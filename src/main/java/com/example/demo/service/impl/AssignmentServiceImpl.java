package com.example.demo.service.impl;

import com.example.demo.entity.Account;
import com.example.demo.entity.Assignment;
import com.example.demo.entity.Classroom;
import com.example.demo.repository.AssignmentRepository;
import com.example.demo.service.AssignmentService;
import com.example.demo.service.ClassroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;


@Service
@Transactional(rollbackFor = {Throwable.class})
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {
    private final AssignmentRepository assignmentRepository;

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

}
