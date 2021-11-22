package com.example.demo.service.impl;

import com.example.demo.common.exception.RTException;
import com.example.demo.common.exception.RecordNotFoundException;
import com.example.demo.entity.Account;
import com.example.demo.entity.Assignment;
import com.example.demo.entity.Classroom;
import com.example.demo.repository.AssignmentRepository;
import com.example.demo.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(rollbackFor = {Throwable.class})
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {
    private final AssignmentRepository assignmentRepository;

    @Override
    public Assignment getAssignment(Long id) {
        return assignmentRepository.findById(id)
                .orElseThrow(()->new RTException(new RecordNotFoundException(id.toString(), Assignment.class.getSimpleName())));
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

}
