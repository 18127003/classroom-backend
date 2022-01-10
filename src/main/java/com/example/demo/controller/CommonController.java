package com.example.demo.controller;

import com.example.demo.common.enums.Role;
import com.example.demo.common.exception.RTException;
import com.example.demo.dto.*;
import com.example.demo.entity.Classroom;
import com.example.demo.mapper.AccountMapper;
import com.example.demo.mapper.ClassroomMapper;
import com.example.demo.mapper.ParticipantMapper;
import com.example.demo.mapper.StudentInfoClassroomMapper;
import com.example.demo.service.AccountService;
import com.example.demo.service.ClassroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@RequestMapping(AbstractServiceEndpoint.CLASS_PATH)
@RequiredArgsConstructor
public class CommonController extends AbstractServiceEndpoint{
    private final ClassroomService classroomService;
    private final ClassroomMapper classroomMapper;
    private final AccountService accountService;
    private final ParticipantMapper participantMapper;
    private final StudentInfoClassroomMapper infoClassroomMapper;

    @GetMapping("test")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("Hello World");
    }

    @PostMapping("create")
    public ResponseEntity<ClassroomDto> createClass(@RequestBody Classroom classroom,
                                                    @AuthenticationPrincipal UUID accountId){
        var account = accountService.getAccountById(accountId);
        return ResponseEntity.ok(classroomMapper.toAssignedClassroomDto(classroomService.createClassroom(classroom, account)));

    }

    @PostMapping("join")
    public ResponseEntity<ClassroomDto> joinClass(@RequestParam String code,
                                                  @RequestParam(required = false) String role,
                                                    @AuthenticationPrincipal UUID accountId){
        if(role==null){
            role = Role.STUDENT.name();
        }
        code = code.replaceAll(" ","+");
        var account = accountService.getAccountById(accountId);
        var classroom = classroomService.joinClassroom(code, Role.valueOf(role.toUpperCase()), account);
        return ResponseEntity.ok(classroomMapper.toAssignedClassroomDto(classroom));
    }
    
    @DeleteMapping("{id}/removeParticipants")
    public ResponseEntity<Void> removeParticipants(@PathVariable Long id, @RequestBody List<UUID> removals){
        classroomService.removeParticipants(id, removals);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("{id}/hideParticipants")
    public ResponseEntity<Void> hideParticipants(@PathVariable Long id, @RequestBody List<UUID> participants){
        classroomService.hideParticipants(id, participants);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("{id}/regenerateCode")
    public ResponseEntity<String> regenerateCode(@PathVariable Long id){
        return ResponseEntity.ok(classroomService.regenerateCode(id));
    }

    @GetMapping("all")
    public ResponseEntity<List<ClassroomDto>> getClasses(@AuthenticationPrincipal UUID accountId){
        return ResponseEntity.ok(classroomService.getAssignedClassrooms(accountId)
                .stream().map(classroomMapper::toAssignedClassroomDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("{id}")
    public ResponseEntity<ClassroomDto> getClassroom(@PathVariable Long id, @AuthenticationPrincipal UUID accountId){
        var participant = classroomService.getAssignedClassroom(id, accountId);
        return ResponseEntity.ok(classroomMapper.toAssignedClassroomDto(participant));
    }

    @GetMapping("{id}/participant")
    public ResponseEntity<List<ParticipantDto>> getParticipants(@PathVariable Long id){
        return ResponseEntity.ok(classroomService.getParticipants(id).stream().map(participantMapper::toParticipantDto)
                .collect(Collectors.toList()));
    }

    @PostMapping("{id}/invite")
    public void inviteParticipants(@RequestBody List<String> invitations, @PathVariable Long id,
                                   @RequestParam String role, HttpServletRequest request) {
        var frontHost = request.getHeader("origin");
        classroomService.sendInvitation(frontHost, invitations, id, Role.valueOf(role.toUpperCase()));
    }

    @GetMapping("{id}/studentInfo/all")
    public ResponseEntity<List<StudentInfoClassroomDto>> getStudentInfo(@PathVariable Long id){
        return ResponseEntity.ok(
                classroomService.getAllStudentInfo(id)
                .stream().map(infoClassroomMapper::toStudentInfoClassroomDto)
                .collect(Collectors.toList())
        );
    }

    @PostMapping("{id}/studentInfo/import")
    public ResponseEntity<Void> upload(@RequestParam("file") final MultipartFile file, @PathVariable Long id) {
        try{
            var classroom = classroomService.getClassroom(id);
            classroomService.importStudentInfo(file, classroom);
            return ResponseEntity.ok().build();
        } catch (IOException e){
            return ResponseEntity.badRequest().build();
        }
    }
}
