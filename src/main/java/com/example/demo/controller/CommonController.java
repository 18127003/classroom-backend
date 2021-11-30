package com.example.demo.controller;

import com.example.demo.common.enums.Role;
import com.example.demo.common.exception.RTException;
import com.example.demo.dto.AccountDto;
import com.example.demo.dto.ClassroomDto;
import com.example.demo.entity.Account;
import com.example.demo.entity.Classroom;
import com.example.demo.mapper.AccountMapper;
import com.example.demo.mapper.ClassroomMapper;
import com.example.demo.service.ClassroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(AbstractServiceEndpoint.CLASS_PATH)
@RequiredArgsConstructor
public class CommonController extends AbstractServiceEndpoint{
    private final ClassroomService classroomService;
    private final ClassroomMapper classroomMapper;
    private final AccountMapper accountMapper;

    @GetMapping("test")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("Hello World");
    }

    @PostMapping("create")
    public ResponseEntity<ClassroomDto> createClass(@RequestBody Classroom classroom,
                                                    @AuthenticationPrincipal Account account){
        return ResponseEntity.ok(classroomMapper.toAssignedClassroomDto(classroomService.createClassroom(classroom, account)));
    }

    @PostMapping("join")
    public ResponseEntity<ClassroomDto> joinClass(@RequestParam String code,
                                                  @RequestParam(required = false) String role,
                                                    @AuthenticationPrincipal Account account){
        if(role==null){
            role = Role.STUDENT.name();
        }
        code = code.replaceAll(" ","+");
        try{
            var classroom = classroomService.joinClassroom(code, Role.valueOf(role.toUpperCase()), account);
            return ResponseEntity.ok(classroomMapper.toAssignedClassroomDto(classroom));
        } catch (RTException e){
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("{id}/removeParticipants")
    public ResponseEntity<Void> removeParticipants(@PathVariable Long id, @RequestBody List<Long> removals){
        try{
            classroomService.removeParticipants(id, removals);
            return ResponseEntity.ok().build();
        } catch (RTException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("{id}/hideParticipants")
    public ResponseEntity<Void> hideParticipants(@PathVariable Long id, @RequestBody List<Long> participants){
        try {
            classroomService.hideParticipants(id, participants);
            return ResponseEntity.ok().build();
        } catch (RTException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("{id}/regenerateCode")
    public ResponseEntity<String> regenerateCode(@PathVariable Long id){
        return ResponseEntity.ok(classroomService.regenerateCode(id));
    }

    @GetMapping("all")
    public ResponseEntity<List<ClassroomDto>> getClasses(@AuthenticationPrincipal Account account){
        return ResponseEntity.ok(classroomService.getAssignedClassrooms(account.getId())
                .stream().map(classroomMapper::toAssignedClassroomDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("{id}")
    public ResponseEntity<ClassroomDto> getClassroom(@PathVariable Long id, @AuthenticationPrincipal Account account){
        try{
            var participant = classroomService.getAssignedClassroom(id, account);
            return ResponseEntity.ok(classroomMapper.toAssignedClassroomDto(participant));
        } catch (RTException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("{id}/participant")
    public ResponseEntity<List<AccountDto>> getParticipants(@PathVariable Long id){
        return ResponseEntity.ok(classroomService.getParticipants(id).stream().map(accountMapper::toParticipantDto)
                .collect(Collectors.toList()));
    }

    @PostMapping("{id}/invite")
    public void inviteParticipants(@RequestBody List<String> invitations, @PathVariable Long id,
                                   @RequestParam String role) {
        classroomService.sendInvitation(invitations, id, Role.valueOf(role.toUpperCase()));
    }

    @PatchMapping("{id}/participant/studentId/update")
    public ResponseEntity<Void> updateStudentId(@PathVariable Long id, @RequestParam String v,
                                                @AuthenticationPrincipal Account account){
        try {
            classroomService.updateStudentId(id, account, v);
            return ResponseEntity.ok().build();
        } catch (RTException e){
            return ResponseEntity.badRequest().build();
        }
    }
}
