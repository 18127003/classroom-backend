package com.example.demo.mapper.decorator;

import com.example.demo.dto.GradeReviewDto;
import com.example.demo.entity.GradeReview;
import com.example.demo.mapper.GradeReviewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class GradeReviewDecorator implements GradeReviewMapper {
    @Autowired
    @Qualifier("delegate")
    private GradeReviewMapper delegate;

    @Override
    public GradeReviewDto toGradeReviewDto(GradeReview gradeReview) {
        var result = delegate.toGradeReviewDto(gradeReview);
        result.setCurrentGrade(gradeReview.getSubmission().getGrade());
        result.setAuthor(gradeReview.getRequestBy().getName());
        var assignment = gradeReview.getSubmission().getAssignment();
        result.setAssignment(assignment.getName());
        result.setAssignmentId(assignment.getId());
        return result;
    }
}
