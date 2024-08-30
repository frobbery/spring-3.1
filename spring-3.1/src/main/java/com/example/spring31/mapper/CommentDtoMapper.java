package com.example.spring31.mapper;

import com.example.spring31.domain.dto.BookDto;
import com.example.spring31.domain.dto.CommentDto;
import com.example.spring31.domain.entity.Book;
import com.example.spring31.domain.entity.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentDtoMapper implements Mapper<Comment, CommentDto> {

    @Override
    public CommentDto mapToDto(Comment objectToMap) {
        return CommentDto.builder()
                .id(objectToMap.getId())
                .text(objectToMap.getText())
                .book(BookDto.builder()
                        .id(objectToMap.getBook().getId())
                        .build())
                .build();
    }

    @Override
    public Comment map(CommentDto objectToMap) {
        return Comment.builder()
                .id(objectToMap.getId())
                .text(objectToMap.getText())
                .book(Book.builder()
                        .id(objectToMap.getBook().getId())
                        .build())
                .build();
    }
}
