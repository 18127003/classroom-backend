package com.example.demo.mapper;

import com.example.demo.dto.CommentDto;
import com.example.demo.entity.Comment;
import com.example.demo.mapper.decorator.CommentDecorator;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
@DecoratedWith(CommentDecorator.class)
public interface CommentMapper {
    CommentDto toCommentDto(Comment comment);
}
