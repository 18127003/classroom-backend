package com.example.demo.controller;

import com.example.demo.dto.AssignmentDto;
import com.example.demo.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(AbstractServiceEndpoint.ASSIGNMENT_PATH)
@RequiredArgsConstructor
public class AssignmentController extends AbstractServiceEndpoint {

    private final AssignmentService assignmentService;


}
