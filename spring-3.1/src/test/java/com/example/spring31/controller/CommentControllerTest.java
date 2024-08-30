package com.example.spring31.controller;

import com.example.spring31.config.YamlPropertySourceFactory;
import com.example.spring31.domain.dto.BookDto;
import com.example.spring31.domain.dto.CommentDto;
import com.example.spring31.services.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@TestPropertySource(value = "/application.yml", factory = YamlPropertySourceFactory.class)
@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Test
    void shouldAddCommentToBook() throws Exception {
        //given
        var bookId = 1L;
        var commentText = "text";
        var commentId = 3L;
        when(commentService.addToBook(CommentDto.builder()
                .text(commentText)
                .book(BookDto.builder()
                        .id(bookId)
                        .build())
                .build())).thenReturn(commentId);

        //when
        MockHttpServletResponse result = mockMvc.perform(post(format("/books/1/comments?commentText=%s", commentText)))
                .andReturn()
                .getResponse();

        //then
        assertThat(result.getContentAsString()).isEqualTo("Saved comment id : 3");
    }

    @Test
    void shouldGetAllCommentsByBookId() throws Exception {
        //given
        var bookId = 1L;
        var commentText = "text";
        var commentId = 3L;
        var commentDto = CommentDto.builder()
                .id(commentId)
                .text(commentText)
                .book(BookDto.builder()
                        .id(bookId)
                        .build())
                .build();
        when(commentService.getAllOfBookWithId(bookId)).thenReturn(List.of(commentDto));

        //when
        MockHttpServletResponse result = mockMvc.perform(get("/books/1/comments"))
                .andReturn()
                .getResponse();

        //then
        assertThat(result.getContentAsString()).isEqualTo("Comments are : " + commentDto);
    }

    @Test
    void shouldDeleteCommentById() throws Exception {
        //when
        MockHttpServletResponse result = mockMvc.perform(delete("/books/1/comments/3"))
                .andReturn()
                .getResponse();

        //then
        assertThat(result.getContentAsString()).isEqualTo("Comment by id 3 is deleted");
        verify(commentService, times(1)).deleteById(3L);
    }

    @Test
    void shouldUpdateCommentTextById() throws Exception {
        //given
        var bookId = 1L;
        var commentText = "text";
        var commentId = 3L;
        var commentDto = CommentDto.builder()
                .id(commentId)
                .text(commentText)
                .book(BookDto.builder()
                        .id(bookId)
                        .build())
                .build();

        //when
        MockHttpServletResponse result = mockMvc.perform(put(format("/books/1/comments/3?newText=%s", commentDto.getText())))
                .andReturn()
                .getResponse();

        //then
        assertThat(result.getContentAsString()).isEqualTo("Comment by id 3 is updated");
        verify(commentService, times(1)).updateTextById(3L, commentDto.getText());
    }

    @Test
    void shouldReturnMessageWhenExceptionOccurred() throws Exception {
        //given
        var exceptionMessage = "message";
        doThrow(new RuntimeException(exceptionMessage)).when(commentService).deleteById(3L);

        //when
        MockHttpServletResponse result = mockMvc.perform(delete("/books/1/comments/3"))
                .andReturn()
                .getResponse();

        //then
        assertThat(result.getContentAsString()).isEqualTo("Error occurred: " + exceptionMessage);
    }
}