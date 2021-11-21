package com.example.demo.controller;

import com.example.demo.common.exception.RTException;
import com.example.demo.dto.AssignmentDto;
import com.example.demo.entity.Account;
import com.example.demo.entity.Assignment;
import com.example.demo.mapper.AssignmentMapper;
import com.example.demo.security.ParticipantInfo;
import com.example.demo.service.AssignmentService;
import com.example.demo.service.ClassroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(AbstractServiceEndpoint.ASSIGNMENT_PATH)
@RequiredArgsConstructor
public class AssignmentController extends AbstractServiceEndpoint {

    private final AssignmentService assignmentService;
    private final ClassroomService classroomService;
    private final AssignmentMapper assignmentMapper;
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
}
