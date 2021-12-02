package com.example.demo.service.impl;

import com.example.demo.common.exception.RTException;
import com.example.demo.common.exception.RecordNotFoundException;
import com.example.demo.dto.SubmissionDto;
import com.example.demo.entity.*;
import com.example.demo.repository.AssignmentRepository;
import com.example.demo.repository.ParticipantRepository;
import com.example.demo.repository.StudentInfoRepository;
import com.example.demo.repository.SubmissionRepository;
import com.example.demo.service.AssignmentService;
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
    private final StudentInfoRepository studentInfoRepository;
    private final SubmissionRepository submissionRepository;
    private final ParticipantRepository participantRepository;
    private final ExcelUtil excelUtil;

    @Override
    public Assignment getAssignment(Long id) {
        return assignmentRepository.findById(id)
                .orElseThrow(()->new RTException(new RecordNotFoundException(id.toString(), Assignment.class.getSimpleName())));
    }

    @Override
    public StudentInfo getStudentInfo(String studentId, Long classroomId) {
        var result = studentInfoRepository.findByStudentId(studentId, classroomId);
        if(result == null){
            throw new RTException(new RecordNotFoundException(studentId, StudentInfo.class.getSimpleName()));
        }
        return result;
    }

    @Override
    public void deleteAllStudentInfo(Long classroomId) {
        var old = getAllStudentInfo(classroomId);
        old.stream().map(StudentInfo::getName).forEach(System.out::println);
        studentInfoRepository.deleteAll(old);
    }

    @Override
    public List<StudentInfo> getAllStudentInfo(Long classroomId) {
        return studentInfoRepository.findAllStudentInfo(classroomId);
    }

    @Override
    public void importStudentInfo(MultipartFile file, Classroom classroom) throws IOException {
        var students = excelUtil.importStudentInfo(file, classroom);

        // ignore existed student info
        var old = getAllStudentInfo(classroom.getId())
                .stream().collect(Collectors.toMap(StudentInfo::getStudentId,k->k));

        // auto sync with participant accounts
        var syncParticipants = participantRepository.getParticipants(classroom.getId())
                .stream().filter(participant -> participant.getStudentId()!=null)
                .collect(Collectors.toMap(Participant::getStudentId,Participant::getAccount));

        students = students.stream()
                .filter(studentInfo -> !old.containsKey(studentInfo.getStudentId())).collect(Collectors.toList());
        students.forEach(studentInfo -> {
            var key = studentInfo.getStudentId();
            if(old.containsKey(key)){
                studentInfo.setId(old.get(key).getId());
            }
            if(syncParticipants.containsKey(key)){
                studentInfo.setClassroomAccount(syncParticipants.get(key));
            }
        });
        studentInfoRepository.saveAll(students);
    }

    @Override
    public void exportTemplateFile(HttpServletResponse response, Long classroomId) throws IOException {
        var assignmentData = studentInfoRepository.getExcelData(classroomId);
        var data = assignmentData.stream()
                .collect(Collectors.toMap(k->k.get(0,Long.class), Arrays::asList,
                        (o, n)-> Stream.of(o,n).flatMap(Collection::stream).collect(Collectors.toList())));
        var studentInfos = studentInfoRepository.findAllStudentInfo(classroomId);
        excelUtil.exportTemplate(response,data, studentInfos);
    }

    @Override
    public void importSubmission(MultipartFile file, Classroom classroom, Long assignmentId) throws IOException {
        var assignment = getAssignment(assignmentId);
        var data = excelUtil.importSubmission(file, assignment);
        var studentInfos = getAllStudentInfo(classroom.getId())
                .stream().filter(studentInfo -> data.containsKey(studentInfo.getStudentId()))
                .collect(Collectors.toList());
        var submissions = studentInfos
                .stream().map(studentInfo->
                        new Submission(data.get(studentInfo.getStudentId()), studentInfo, assignment))
                .collect(Collectors.toList());
        // delete old
        var old = submissionRepository.getAllSubmission(assignmentId);
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
    public List<Assignment> getAllAssignments(Long classroomId) {
        return assignmentRepository.getAll(classroomId);
    }

    @Override
    public Assignment addAssignment(Assignment assignment, Account creator, Classroom classroom) {
        assignment.setClassroom(classroom);
        assignment.setCreator(creator);
        Date current = Date.from(Instant.now());
        assignment.setCreatedAt(current);
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
    public StudentInfo addStudentInfo(StudentInfo studentInfo) {
        return studentInfoRepository.save(studentInfo);
    }

    @Override
    public Submission addSubmission(SubmissionDto submissionDto) {
        var assignment = getAssignment(submissionDto.getAssignmentId());
        var studentInfo = getStudentInfo(submissionDto.getStudentId(), submissionDto.getClassroomId());
        var submission = new Submission(submissionDto.getGrade(), studentInfo, assignment);
        return submissionRepository.save(submission);
    }
}
