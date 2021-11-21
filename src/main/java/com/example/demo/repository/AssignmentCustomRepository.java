package com.example.demo.repository;

import com.example.demo.entity.Assignment;

import java.util.List;

public interface AssignmentCustomRepository {
    List<Assignment> getAll(Long classroomId);
}
