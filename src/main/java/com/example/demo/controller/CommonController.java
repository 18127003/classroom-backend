package com.example.demo.controller;

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
        return ResponseEntity.ok(classroomMapper.toClassroomDto(classroomService.createClassroom(classroom, account)));
    }

    @PostMapping("join")
    public ResponseEntity<ClassroomDto> joinClass(@RequestParam String code,
                                                    @AuthenticationPrincipal Account account){
        try{
            var classroom = classroomService.joinClassroom(code, account);
            return ResponseEntity.ok(classroomMapper.toClassroomDto(classroom));
        } catch (RTException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("all")
    public ResponseEntity<List<ClassroomDto>> getClasses(@AuthenticationPrincipal Account account){
        return ResponseEntity.ok(classroomService.getAssignedClassrooms(account.getId())
                .stream().map(classroomMapper::toAssignedClassroomDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("{id}/participant")
    public ResponseEntity<List<AccountDto>> getParticipants(@PathVariable Long id){
        return ResponseEntity.ok(classroomService.getParticipants(id).stream().map(accountMapper::toParticipantDto)
                .collect(Collectors.toList()));
    }
}
