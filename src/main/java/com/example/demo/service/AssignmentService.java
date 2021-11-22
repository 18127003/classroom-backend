package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Assignment;
import com.example.demo.entity.Classroom;

import java.util.List;

public interface AssignmentService {
    Assignment getAssignment(Long id);

    List<Assignment> getAllAssignments(Long classroomId);

    Assignment addAssignment(Assignment assignment, Account creator, Classroom classroom);

    void removeAssignment(Long id);

    Assignment updateAssignment(Long id, Assignment update);

    void updatePosition(Long id, List<Long> update);
}
