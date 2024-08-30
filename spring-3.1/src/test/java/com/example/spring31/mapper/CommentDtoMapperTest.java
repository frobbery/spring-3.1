package com.example.spring31.mapper;

import com.example.spring31.domain.dto.BookDto;
import com.example.spring31.domain.dto.CommentDto;
import com.example.spring31.domain.entity.Book;
import com.example.spring31.domain.entity.Comment;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CommentDtoMapperTest {

    private final CommentDtoMapper sut = new CommentDtoMapper();

    @Test
    void mapToDto() {
        //given
        var objectToMap = Comment.builder()
                .id(1L)
                .text("text")
                .book(Book.builder().id(45L).build())
                .build();
        var expectedDto = CommentDto.builder()
                .id(objectToMap.getId())
                .text(objectToMap.getText())
                .book(BookDto.builder().id(objectToMap.getBook().getId()).build())
                .build();

        //when
        var mappedObject = sut.mapToDto(objectToMap);

        //then
        assertThat(mappedObject)
                .usingRecursiveComparison()
                .isEqualTo(expectedDto);
    }

    @Test
    void map() {
        //given
        var objectToMap = CommentDto.builder()
                .id(1L)
                .text("text")
                .book(BookDto.builder().id(45L).build())
                .build();
        var expectedEntity = Comment.builder()
                .id(objectToMap.getId())
                .text(objectToMap.getText())
                .book(Book.builder().id(objectToMap.getBook().getId()).build())
                .build();

        //when
        var mappedObject = sut.map(objectToMap);

        //then
        assertThat(mappedObject)
                .usingRecursiveComparison()
                .isEqualTo(expectedEntity);
    }
}