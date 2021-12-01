package com.example.demo.repository;

import com.example.demo.entity.Submission;

import java.util.List;

public interface SubmissionCustomRepository {
    List<Submission> getAllSubmission(Long assignmentId);
}
