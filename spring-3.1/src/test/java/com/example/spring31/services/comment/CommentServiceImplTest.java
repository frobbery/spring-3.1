package com.example.spring31.services.comment;

import com.example.spring31.domain.dto.CommentDto;
import com.example.spring31.domain.entity.Book;
import com.example.spring31.domain.entity.Comment;
import com.example.spring31.mapper.Mapper;
import com.example.spring31.repository.BookRepository;
import com.example.spring31.repository.CommentRepository;
import com.example.spring31.services.impl.CommentServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Сервис для работы с комментариями должен:")
@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private Mapper<Comment, CommentDto> commentDtoMapper;

    @InjectMocks
    private CommentServiceImpl sut;

    @Test
    @DisplayName("Должен добавлять комментарий к книге")
    void shouldAddCommentToBook() {
        //given
        var commentDto = mock(CommentDto.class);
        var commentToBeSaved = Comment.builder()
                .text("text")
                .build();
        var commentFromDao = Comment.builder()
                .id(2L)
                .text("text")
                .build();
        var book = Book.builder()
                .id(1L)
                .build();
        var comment = Comment.builder()
                .text("text")
                .book(book)
                .build();
        when(commentDtoMapper.map(commentDto)).thenReturn(comment);
        when(commentRepository.save(commentToBeSaved)).thenReturn(commentFromDao);
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        //when
        var result = sut.addToBook(commentDto);

        //then
        assertEquals(commentFromDao.getId(), result);
        verify(commentRepository, times(1)).updateBookById(commentFromDao.getId(), book);
    }

    @Test
    @DisplayName("Должен получать комментарий по id")
    void shouldGetCommentById() {
        //given
        var expectedComment = mock(Comment.class);
        var expectedCommentDto = mock(CommentDto.class);
        when(commentRepository.findById(expectedComment.getId())).thenReturn(Optional.of(expectedComment));
        when(commentDtoMapper.mapToDto(expectedComment)).thenReturn(expectedCommentDto);

        //when
        var actualCommentDto = sut.getById(expectedComment.getId());

        //then
        assertThat(actualCommentDto)
                .isPresent()
                .contains(expectedCommentDto);
    }

    @Test
    @DisplayName("Должен получать все комментарии по книге")
    void shouldGetAllCommentsOfBook() {
        //given
        var bookId = 1L;
        var expectedComment = mock(Comment.class);
        var expectedCommentDto = mock(CommentDto.class);
        when(commentRepository.findAllByBookId(bookId)).thenReturn(List.of(expectedComment));
        when(commentDtoMapper.mapToDto(expectedComment)).thenReturn(expectedCommentDto);

        //when
        var actualCommentDtos = sut.getAllOfBookWithId(bookId);

        //then
        assertThat(actualCommentDtos)
                .containsAll(List.of(expectedCommentDto));
    }

    @Test
    @DisplayName("Должен обновлять текст комментария")
    void shouldUpdateCommentTextById() {
        //given
        var comment = mock(Comment.class);

        //when
        sut.updateTextById(comment.getId(), comment.getText());

        //then
        verify(commentRepository, times(1)).updateTextById(comment.getId(), comment.getText());
    }

    @Test
    @DisplayName("Должен удалять комментарий по id")
    void deleteCommentById() {
        //given
        var comment = mock(Comment.class);

        //when
        sut.deleteById(comment.getId());

        //then
        verify(commentRepository, times(1)).deleteById(comment.getId());
    }
}