package com.example.demo.controller;

import com.example.demo.common.enums.Role;
import com.example.demo.common.exception.RTException;
import com.example.demo.dto.*;
import com.example.demo.entity.Assignment;
import com.example.demo.entity.GradeReview;
import com.example.demo.entity.StudentInfo;
import com.example.demo.mapper.AssignmentMapper;
import com.example.demo.mapper.GradeReviewMapper;
import com.example.demo.mapper.StudentInfoMapper;
import com.example.demo.mapper.SubmissionMapper;
import com.example.demo.security.ParticipantInfo;
import com.example.demo.service.AccountService;
import com.example.demo.service.AssignmentService;
import com.example.demo.service.ClassroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(AbstractServiceEndpoint.ASSIGNMENT_PATH)
@RequiredArgsConstructor
public class AssignmentController extends AbstractServiceEndpoint {

    private final AssignmentService assignmentService;
    private final ClassroomService classroomService;
    private final AccountService accountService;
    private final AssignmentMapper assignmentMapper;
    private final SubmissionMapper submissionMapper;
    private final GradeReviewMapper gradeReviewMapper;

    /**
     * cai nay de lay thong tin classroom cua assignment
     * goi participantInfo.getClassroom()
     * **/
    private final ParticipantInfo participantInfo;


    @PostMapping("create")
    public ResponseEntity<AssignmentDto> addAssignment(@RequestBody Assignment assignment){
        try {
            var account = participantInfo.getAccount();
            var classroom = classroomService.getClassroom(participantInfo.getClassroom().getId());
            return ResponseEntity.ok(assignmentMapper.toAssignmentDto(
                    assignmentService.addAssignment(assignment, account, classroom)));
        } catch (RTException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("all")
    public ResponseEntity<List<AssignmentDto>> allAssignment(){
        return ResponseEntity.ok(
                assignmentService.getAllAssignments(participantInfo.getClassroom().getId())
                        .stream().map(assignmentMapper::toAssignmentDto)
                        .collect(Collectors.toList())
        );
    }

    @PatchMapping("updatePosition")
    public ResponseEntity<Void> updatePosition(@RequestBody List<Long> update){
        assignmentService.updatePosition(participantInfo.getClassroom().getId(), update);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{id}/detail")
    public ResponseEntity<AssignmentDto> getAssignment(@PathVariable Long id){
        try{
            var assignment = assignmentService.getAssignment(id);
            return ResponseEntity.ok(assignmentMapper.toAssignmentDto(assignment));
        } catch (RTException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("{id}/remove")
    public ResponseEntity<Void> removeAssignment(@PathVariable Long id){
        try{
            assignmentService.removeAssignment(id);
            return ResponseEntity.ok().build();
        } catch (RTException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("{id}/update")
    public ResponseEntity<AssignmentDto> updateAssignment(@PathVariable Long id, @RequestBody Assignment update){
        try{
            var assignment = assignmentService.updateAssignment(id, update);
            return ResponseEntity.ok(assignmentMapper.toAssignmentDto(assignment));
        } catch (RTException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("*/submission/create")
    public ResponseEntity<SubmissionDto> addSubmission(@RequestBody SubmissionDto submission){
        try{
            return ResponseEntity.ok(submissionMapper.toSubmissionDto(assignmentService.addSubmission(submission, participantInfo.getClassroom().getId())));
        } catch (RTException e){
            return ResponseEntity.badRequest().build();
        }

    }

    @PatchMapping("*/submission/{id}/update")
    public ResponseEntity<SubmissionDto> updateSubmission(@PathVariable Long id, @RequestParam String grade){
        try{
            var gradeNum = Integer.valueOf(grade);
            return ResponseEntity.ok(submissionMapper.toSubmissionDto(assignmentService.updateSubmissionGrade(id, gradeNum)));
        } catch (NumberFormatException | RTException numberFormatException){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("template/export")
    public ResponseEntity<Void> exportTemplate(final HttpServletResponse response){
        try {
            assignmentService.exportTemplateFile(response,participantInfo.getClassroom().getId());
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("{id}/submission/import")
    public ResponseEntity<Void> importSubmission(@PathVariable Long id, @RequestParam("file") MultipartFile file){
        try {
            assignmentService.importSubmission(file, participantInfo.getClassroom(), id);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("{id}/submission/finalize")
    public ResponseEntity<Void> finalizeGradeComposition(@PathVariable Long id){
        try {
            assignmentService.finalizeGrade(id);
            return ResponseEntity.ok().build();
        } catch (RTException e){
            return ResponseEntity.badRequest().build();
        }
    }

    // For student only
    @GetMapping("submission/overall")
    public ResponseEntity<OverallGradeDto> getStudentOverallGrade(@AuthenticationPrincipal Long accountId){
        var result = assignmentService.getOverallGrade(accountId, participantInfo.getClassroom().getId());
        return ResponseEntity.ok(result);
    }

    // For student only
    @GetMapping("submission/all")
    public ResponseEntity<List<SubmissionDto>> getStudentGrade(){
        try{
            var account = participantInfo.getAccount();
            var classroomId = participantInfo.getClassroom().getId();
            var result = assignmentService.getGradeOfClassByStudent(account, classroomId);
            return ResponseEntity.ok(result.stream().map(submissionMapper::toSubmissionDto).collect(Collectors.toList()));
        } catch (RTException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("{id}/submission/review/create")
    public ResponseEntity<GradeReviewDto> createGradeReview(@RequestBody GradeReview gradeReview, @PathVariable Long id){
        var account = participantInfo.getAccount();
        var studentInfo = account.getStudentInfo();
        if (studentInfo == null){
            return ResponseEntity.badRequest().build();
        }
        var existedPendingReview = assignmentService.getPendingGradeReview(id, studentInfo.getStudentId());
        if (existedPendingReview != null){
            return ResponseEntity.badRequest().build();
        }
        var submission = assignmentService.getSubmission(id, studentInfo.getStudentId());
        var created = assignmentService.addGradeReview(gradeReview, submission, account);
        return ResponseEntity.ok(gradeReviewMapper.toGradeReviewDto(created));
    }

    @GetMapping("submission/review/all")
    public ResponseEntity<List<GradeReviewDto>> getGradeReview(){
        var account = participantInfo.getAccount();
        var classroomId = participantInfo.getClassroom().getId();
        List<GradeReview> gradeReviews;
        if (participantInfo.getRole().equals(Role.STUDENT)){
            var studentInfo = account.getStudentInfo();
            if (studentInfo==null){
                return ResponseEntity.badRequest().build();
            }
            var studentId = studentInfo.getStudentId();
            gradeReviews = assignmentService.getAllGradeReviewOfAccount(studentId, classroomId);
        } else {
            gradeReviews = assignmentService.getAllGradeReviewOfClass(classroomId);
        }
        return ResponseEntity.ok(gradeReviews.stream().map(gradeReviewMapper::toGradeReviewDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("{id}/submission/filled")
    public ResponseEntity<SubmissionFillCheckResponse> checkFilled(@PathVariable Long id){
        var unFillStudents = assignmentService.checkFillSubmission(id);
        boolean fill = unFillStudents.isEmpty();
        return ResponseEntity.ok(new SubmissionFillCheckResponse(fill, unFillStudents));
    }
}
