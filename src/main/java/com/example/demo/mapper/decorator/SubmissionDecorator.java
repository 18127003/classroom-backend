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
        var assignment = submission.getGradeComposition().getAssignment();
        result.setAssignmentId(assignment.getId());
        result.setStudentId(submission.getStudentInfoClassroom().getStudentInfo().getStudentId());
        result.setMaxGrade(assignment.getPoints());
        return result;
    }
}
