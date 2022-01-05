package com.example.demo.mapper.decorator;

import com.example.demo.dto.CommentDto;
import com.example.demo.entity.Comment;
import com.example.demo.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class CommentDecorator implements CommentMapper {
    @Autowired
    @Qualifier("delegate")
    private CommentMapper delegate;

    @Override
    public CommentDto toCommentDto(Comment comment) {
        var result = delegate.toCommentDto(comment);
        result.setBy(comment.getAuthor().getName());
        result.setReviewId(comment.getGradeReview().getId());
        return result;
    }
}
