package com.example.demo.repository.impl;

import com.example.demo.entity.Assignment;
import com.example.demo.entity.QAssignment;
import com.example.demo.repository.AssignmentCustomRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AssignmentRepositoryImpl extends AbstractRepositoryImpl<Assignment> implements AssignmentCustomRepository {
    @Override
    public List<Assignment> getAll(Long classroomId) {
        return selectFrom(QAssignment.assignment).where(QAssignment.assignment.classroom.id.eq(classroomId)).fetch();
    }
}
