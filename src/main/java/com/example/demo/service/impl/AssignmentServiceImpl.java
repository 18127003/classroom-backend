package com.example.demo.service.impl;

import com.example.demo.common.enums.AssignmentStatus;
import com.example.demo.common.enums.GradeReviewStatus;
import com.example.demo.common.enums.Role;
import com.example.demo.common.exception.RTException;
import com.example.demo.common.exception.RecordNotFoundException;
import com.example.demo.dto.OverallGradeDto;
import com.example.demo.dto.SubmissionDto;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.AssignmentService;
import com.example.demo.service.NotificationService;
import com.example.demo.util.ExcelUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@Transactional(rollbackFor = {Throwable.class})
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {
    private final AssignmentRepository assignmentRepository;
    private final StudentInfoClassroomRepository studentInfoClassroomRepository;
    private final SubmissionRepository submissionRepository;
    private final GradeReviewRepository gradeReviewRepository;
    private final CommentRepository commentRepository;
    private final NotificationService notificationService;
    private final ExcelUtil excelUtil;

    @Override
    public Assignment getAssignment(Long id) {
        return assignmentRepository.findById(id)
                .orElseThrow(()->new RTException(new RecordNotFoundException(id.toString(), Assignment.class.getSimpleName())));
    }

    @Override
    public void exportTemplateFile(HttpServletResponse response, Long classroomId) throws IOException {
        var assignmentData = studentInfoClassroomRepository.getExcelData(classroomId);
        var data = assignmentData.stream()
                .collect(Collectors.toMap(k->k.get(0,Long.class), Arrays::asList,
                        (o, n)-> Stream.of(o,n).flatMap(Collection::stream).collect(Collectors.toList())));
        var studentInfos = studentInfoClassroomRepository.findAllStudentInfo(classroomId);
        excelUtil.exportTemplate(response,data, studentInfos);
    }

    @Override
    public void importSubmission(MultipartFile file, Classroom classroom, Long assignmentId) throws IOException {
        var assignment = getAssignment(assignmentId);
        var data = excelUtil.importSubmission(file, assignment.getName());

        // get student class info of submitted ids
        var submittedInfos = studentInfoClassroomRepository.findByListStudentId(
                data.keySet(), classroom.getId()
        );

        var submissions = submittedInfos
                .stream().map(studentInfo->
                        new Submission(data.get(studentInfo.getStudentInfo().getStudentId()), studentInfo, assignment))
                .collect(Collectors.toList());
        // delete old and rewrite
        var old = submissionRepository.getSubmissionByInfoList(submittedInfos, assignmentId);
        submissionRepository.deleteAll(old);
        submissionRepository.saveAll(submissions);
    }

    @Override
    public Submission updateSubmissionGrade(Long submissionId, Integer grade) {
        var submission = submissionRepository.findById(submissionId)
                .orElseThrow(()->new RTException(new RecordNotFoundException(submissionId.toString(), Submission.class.getSimpleName())));
        submission.setGrade(grade);
        return submissionRepository.save(submission);
    }

    @Override
    public OverallGradeDto getOverallGrade(Long accountId, Long classroomId) {
        var result = submissionRepository.getStudentOverallGrade(accountId, classroomId);
        return new OverallGradeDto(result.get(0, Integer.class), result.get(1, Integer.class));
    }

    @Override
    public List<Submission> getGradeOfClassByStudent(Account account, Long classroomId) {
        var studentInfo = account.getStudentInfo();
        if (studentInfo==null){
            throw new RTException(new RecordNotFoundException(null, StudentInfo.class.getSimpleName()));
        }
        return submissionRepository.getSubmissionOfStudentByStatus(classroomId, studentInfo.getStudentId(),
                AssignmentStatus.FINAL);
    }

    @Override
    public void finalizeGrade(Long assignmentId) {
        var assignment = getAssignment(assignmentId);
        assignment.setStatus(AssignmentStatus.FINAL);
        assignmentRepository.save(assignment);
        // Notify
        var accounts = assignment.getClassroom().getParticipants().stream()
                .filter(participant -> participant.getRole().equals(Role.STUDENT))
                .map(Participant::getAccount)
                .collect(Collectors.toList());
        String content = String.format("%s - %s grade has been marked as final.",
                assignment.getClassroom().getName(), assignment.getName());
        notificationService.sendNotification(accounts, content);
    }

    @Override
    public Submission getSubmission(Long assignmentId, String studentId) {
        var result = submissionRepository.getSubmissionByStudentId(assignmentId, studentId);
        if (result == null){
            throw new RTException(new RecordNotFoundException(null, Submission.class.getSimpleName()));
        }
        return result;
    }

    @Override
    public GradeReview addGradeReview(GradeReview gradeReview, Submission submission, Account account) {
        gradeReview.setSubmission(submission);
        gradeReview.setRequestBy(account);
        gradeReview.setStatus(GradeReviewStatus.PENDING);
        // Notify
        String content = String.format("A grade review for %s has been requested.",
                gradeReview.getSubmission().getAssignment().getName());
        var accounts = submission.getAssignment().getClassroom().getParticipants()
                .stream().filter(participant -> participant.getRole().equals(Role.TEACHER))
                .map(Participant::getAccount)
                .collect(Collectors.toList());
        notificationService.sendNotification(accounts, content);
        return gradeReviewRepository.save(gradeReview);
    }

    @Override
    public GradeReview getPendingGradeReview(Long assignmentId, String studentId) {
        return gradeReviewRepository.getByStatus(assignmentId, studentId, GradeReviewStatus.PENDING);
    }

    @Override
    public List<GradeReview> getAllGradeReview(Long assignmentId) {
        return null;
    }

    @Override
    public List<GradeReview> getAllGradeReviewOfClass(Long classroomId) {
        return gradeReviewRepository.getAllOfClassroom(classroomId);
    }

    @Override
    public List<GradeReview> getAllGradeReviewOfAccount(String studentId, Long classroomId) {
        return gradeReviewRepository.getAllOfStudent(studentId, classroomId);
    }

    @Override
    public List<String> checkFillSubmission(Long assignmentId) {
        return submissionRepository.checkNotSubmitStudents(assignmentId);
    }

    @Override
    public Comment createReviewComment(Comment comment, Long reviewId, Account account) {
        var gradeReview = getGradeReview(reviewId);
        comment.setGradeReview(gradeReview);
        comment.setAuthor(account);
        return commentRepository.save(comment);
    }

    @Override
    public Submission finalizeGradeReview(Long gradeReviewId, Integer grade) {
        var gradeReview = getGradeReview(gradeReviewId);
        var submission = gradeReview.getSubmission();
        submission.setGrade(grade);
        gradeReview.setStatus(GradeReviewStatus.ACCEPTED);
        gradeReviewRepository.save(gradeReview);
        // Notify
        String content = String.format("Grade review for %s has been finalize.", submission.getAssignment().getName());
        notificationService.sendNotification(Collections.singletonList(gradeReview.getRequestBy()), content);
        return submission;
    }

    @Override
    public GradeReview getGradeReview(Long gradeReviewId) {
        return gradeReviewRepository.findById(gradeReviewId)
                .orElseThrow(()->new RTException(new RecordNotFoundException(gradeReviewId.toString(), GradeReview.class.getSimpleName())));
    }


    @Override
    public List<Assignment> getAllAssignments(Long classroomId) {
        return assignmentRepository.getAll(classroomId);
    }

    @Override
    public Assignment addAssignment(Assignment assignment, Account creator, Classroom classroom) {
        assignment.setClassroom(classroom);
        assignment.setCreator(creator);
//        Date current = Date.from(Instant.now());
//        assignment.setCreatedAt(current);
        assignment.setStatus(AssignmentStatus.GRADING);
        return assignmentRepository.save(assignment);
    }

    @Override
    public void removeAssignment(Long id) {
        var assignment = getAssignment(id);
        assignmentRepository.delete(assignment);
    }

    @Override
    public Assignment updateAssignment(Long id, Assignment update) {
        var assignment = getAssignment(id);
        assignment.setName(update.getName());
        assignment.setDeadline(update.getDeadline());
        assignment.setDescription(update.getDescription());
        assignment.setPoints(update.getPoints());
        return assignmentRepository.save(assignment);
    }

    @Override
    public void updatePosition(Long id, List<Long> update) {
        var assignments = getAllAssignments(id);
        var updateMap = update.stream().collect(Collectors.toMap((k)->k, update::indexOf));
        assignments.forEach(assignment -> assignment.setPosition(updateMap.get(assignment.getId())));
        assignmentRepository.saveAll(assignments);
    }

    @Override
    public StudentInfoClassroom addStudentInfo(StudentInfoClassroom studentInfo) {
        return studentInfoClassroomRepository.save(studentInfo);
    }

    @Override
    public Submission addSubmission(SubmissionDto submissionDto, Long classroomId) {
        var assignment = getAssignment(submissionDto.getAssignmentId());
        var studentInfo = studentInfoClassroomRepository.findByStudentId(submissionDto.getStudentId(), classroomId);
        var submission = new Submission(submissionDto.getGrade(), studentInfo, assignment);
        return submissionRepository.save(submission);
    }
}
