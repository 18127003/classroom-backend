package com.example.demo.controller;

import com.example.demo.common.exception.RTException;
import com.example.demo.dto.AssignmentDto;
import com.example.demo.dto.StudentInfoDto;
import com.example.demo.dto.SubmissionDto;
import com.example.demo.entity.Account;
import com.example.demo.entity.Assignment;
import com.example.demo.entity.StudentInfo;
import com.example.demo.mapper.AssignmentMapper;
import com.example.demo.mapper.StudentInfoMapper;
import com.example.demo.mapper.SubmissionMapper;
import com.example.demo.security.ParticipantInfo;
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
    private final AssignmentMapper assignmentMapper;
    private final StudentInfoMapper studentInfoMapper;
    private final SubmissionMapper submissionMapper;

    /**
     * cai nay de lay thong tin classroom cua assignment
     * goi participantInfo.getClassroom()
     * **/
    private final ParticipantInfo participantInfo;


    @PostMapping("create")
    public ResponseEntity<AssignmentDto> addAssignment(@RequestBody Assignment assignment
            , @AuthenticationPrincipal Account account){
        try {
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

    // for test only
    @PostMapping("studentInfo/create")
    public ResponseEntity<StudentInfoDto> addStudentInfo(@RequestBody StudentInfo studentInfo){
        studentInfo.setClassroom(participantInfo.getClassroom());
        return ResponseEntity.ok(studentInfoMapper.toStudentInfoDto(assignmentService.addStudentInfo(studentInfo)));
    }

    @GetMapping("studentInfo/all")
    public ResponseEntity<List<StudentInfoDto>> getStudentInfo(){
        return ResponseEntity.ok(
                assignmentService.getAllStudentInfo(participantInfo.getClassroom().getId())
                .stream().map(studentInfoMapper::toStudentInfoDto)
                .collect(Collectors.toList())
        );
    }

    @PostMapping("*/submission/create")
    public ResponseEntity<SubmissionDto> addSubmission(@RequestBody SubmissionDto submission){
        try{
            return ResponseEntity.ok(submissionMapper.toSubmissionDto(assignmentService.addSubmission(submission)));
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

    @PostMapping("studentInfo/import")
    public ResponseEntity<Void> upload(@RequestParam("file") final MultipartFile file) {
        try{
            assignmentService.importStudentInfo(file, participantInfo.getClassroom());
            return ResponseEntity.ok().build();
        } catch (IOException e){
            return ResponseEntity.badRequest().build();
        }

    }

    @GetMapping("/template/export")
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

    @GetMapping("test")
    public void test(){
        assignmentService.test();
    }
}
