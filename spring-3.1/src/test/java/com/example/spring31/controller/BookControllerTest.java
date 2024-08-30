package com.example.spring31.controller;

import com.example.spring31.config.YamlPropertySourceFactory;
import com.example.spring31.domain.dto.AuthorDto;
import com.example.spring31.domain.dto.BookDto;
import com.example.spring31.domain.dto.GenreDto;
import com.example.spring31.services.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@TestPropertySource(value = "/application.yml", factory = YamlPropertySourceFactory.class)
@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    void shouldSaveBook() throws Exception {
        //given
        var expectedBookDto = BookDto.builder()
                .name("bookName")
                .author(AuthorDto.builder()
                        .fullName("authorFullName")
                        .build())
                .genres(List.of(GenreDto.builder()
                                .genreName("genreName1")
                                .build(),
                        GenreDto.builder()
                                .genreName("genreName2")
                                .build()))
                .build();
        var bookId = 1L;
        when(bookService.save(eq((expectedBookDto)))).thenReturn(bookId);

        //when
        MockHttpServletResponse result = mockMvc.perform(post(format("/books?bookName=%s&authorFullName=%s&genreNames=%s,%s",
                        "bookName", "authorFullName", "genreName1", "genreName2")))
                .andReturn()
                .getResponse();

        //then
        assertThat(result.getContentAsString()).isEqualTo("Saved book id : 1");
    }

    @ParameterizedTest
    @MethodSource("getBookById")
    void shouldGetBookById(Optional<BookDto> foundBook, String expectedResult) throws Exception {
        //given
        var bookId = 1L;
        when(bookService.getById(bookId)).thenReturn(foundBook);

        //when
        MockHttpServletResponse result = mockMvc.perform(get("/books/1"))
                .andReturn()
                .getResponse();

        //then
        assertThat(result.getContentAsString()).isEqualTo(expectedResult);
    }

    private static Stream<Arguments> getBookById() {
        var bookDto =  bookDto();
        return Stream.of(
                Arguments.of(Optional.empty(), "Book by id 1 not found"),
                Arguments.of(Optional.of(bookDto), "Book by id 1: " + bookDto)
                );
    }

    @Test
    void shouldGetAllBooksById() throws Exception {
        //given
        var bookDto = bookDto();
        when(bookService.getAll()).thenReturn(List.of(bookDto));

        //when
        MockHttpServletResponse result = mockMvc.perform(get("/books"))
                .andReturn()
                .getResponse();

        //then
        assertThat(result.getContentAsString()).isEqualTo("Found books are :\n" + bookDto);
    }

    @Test
    void shouldUpdateBookById() throws Exception {
        //given
        var expectedBookDto = BookDto.builder()
                .id(1L)
                .name("bookName")
                .author(AuthorDto.builder()
                        .fullName("authorFullName")
                        .build())
                .genres(List.of(GenreDto.builder()
                                .genreName("genreName1")
                                .build(),
                        GenreDto.builder()
                                .genreName("genreName2")
                                .build()))
                .build();

        //when
        MockHttpServletResponse result = mockMvc.perform(put(format("/books/1?bookName=%s&authorFullName=%s&genreNames=%s,%s",
                        "bookName", "authorFullName", "genreName1", "genreName2")))
                .andReturn()
                .getResponse();

        //then
        assertThat(result.getContentAsString()).isEqualTo("Book by id 1 is updated");
        verify(bookService, times(1)).updateById(expectedBookDto);
    }

    @Test
    void shouldDeleteBookById() throws Exception {
        //when
        MockHttpServletResponse result = mockMvc.perform(delete("/books/1"))
                .andReturn()
                .getResponse();

        //then
        assertThat(result.getContentAsString()).isEqualTo("Book by id 1 is deleted");
        verify(bookService, times(1)).deleteById(1L);
    }

    @Test
    void shouldReturnMessage_whenErrorOccurrs() throws Exception {
        //given
        var exceptionMessage = "message";
        doThrow(new RuntimeException(exceptionMessage)).when(bookService).deleteById(1L);

        //when
        MockHttpServletResponse result = mockMvc.perform(delete("/books/1"))
                .andReturn()
                .getResponse();

        //then
        assertThat(result.getContentAsString()).isEqualTo("Error occurred: " + exceptionMessage);
    }

    private static BookDto bookDto() {
        return BookDto.builder()
                .id(1L)
                .name("bookName")
                .author(AuthorDto.builder()
                        .id(2L)
                        .fullName("authorFullName")
                        .build())
                .genres(List.of(GenreDto.builder()
                                .id(3L)
                                .genreName("genreName1")
                                .build(),
                        GenreDto.builder()
                                .id(4L)
                                .genreName("genreName2")
                                .build()))
                .build();
    }
}