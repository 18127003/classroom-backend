package com.example.demo.mapper.decorator;

import com.example.demo.dto.SubmissionDto;
import com.example.demo.entity.Submission;
import com.example.demo.mapper.SubmissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class SubmissionDecorator implements SubmissionMapper {
    @Autowired
    @Qualifier("delegate")
    private SubmissionMapper delegate;

    @Override
    public SubmissionDto toSubmissionDto(Submission submission) {
        var result = delegate.toSubmissionDto(submission);
        result.setAssignmentId(submission.getAssignment().getId());
        result.setStudentId(submission.getStudentInfo().getStudentId());
        result.setClassroomId(submission.getStudentInfo().getClassroom().getId());
        result.setMaxGrade(submission.getAssignment().getPoints());
        return result;
    }
}
