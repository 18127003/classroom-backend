package com.example.demo.repository;

import com.example.demo.entity.Submission;
import com.querydsl.core.Tuple;

import java.util.List;

public interface SubmissionCustomRepository {
    List<Submission> getAllSubmission(Long assignmentId);


}
