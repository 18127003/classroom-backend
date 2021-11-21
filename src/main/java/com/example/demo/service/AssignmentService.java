package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Assignment;
import com.example.demo.entity.Classroom;

import java.util.List;

public interface AssignmentService {
    List<Assignment> getAllAssignments(Long classroomId);

    Assignment addAssignment(Assignment assignment, Account creator, Classroom classroom);

}
