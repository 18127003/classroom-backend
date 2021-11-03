package com.example.demo.controller;

import com.example.demo.dto.ClassroomDto;
import com.example.demo.entity.Classroom;
import com.example.demo.mapper.ClassroomMapper;
import com.example.demo.service.ClassroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(AbstractServiceEndpoint.CLASS_PATH)
@RequiredArgsConstructor
public class CommonController extends AbstractServiceEndpoint{
    private final ClassroomService classroomService;
    private final ClassroomMapper classroomMapper;

    @GetMapping("test")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("Hello World");
    }

    @PostMapping("create")
    public ResponseEntity<ClassroomDto> createClass(@RequestBody Classroom classroom){
        return ResponseEntity.ok(classroomMapper.toClassroomDto(classroomService.createClassroom(classroom)));
    }

    @GetMapping("all")
    public ResponseEntity<List<ClassroomDto>> getClasses(){
        return ResponseEntity.ok(classroomService.getClassrooms().stream().map(classroomMapper::toClassroomDto)
                .collect(Collectors.toList()));
    }
}
