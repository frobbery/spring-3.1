package com.example.spring31.services.book;

import com.example.spring31.domain.dto.BookDto;
import com.example.spring31.domain.entity.Author;
import com.example.spring31.domain.entity.Book;
import com.example.spring31.domain.entity.Genre;
import com.example.spring31.mapper.Mapper;
import com.example.spring31.repository.BookRepository;
import com.example.spring31.services.AuthorService;
import com.example.spring31.services.GenreService;
import com.example.spring31.services.impl.BookServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Сервис для работы с книгами должен:")
@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorService authorService;

    @Mock
    private GenreService genreService;

    @Mock
    private Mapper<Book, BookDto> bookDtoMapper;

    @InjectMocks
    private BookServiceImpl sut;

    @Test
    @DisplayName("Должен сохранять книгу")
    void shouldSaveBook() {
        //given
        var bookDto = mock(BookDto.class);
        var bookToBeSaved = Book.builder()
                .name("name")
                .build();
        var author = Author.builder()
                .fullName("fullName");
        var genre = Genre.builder()
                .genreName("genreName");
        var bookFromDao = Book.builder()
                .id(1L)
                .name("name")
                .build();
        var authorFromService = author
                .id(2L)
                .build();
        var genreFromService = genre
                .id(3L)
                .build();
        var book = Book.builder()
                .name("name")
                .author(author.build())
                .genres(List.of(genre.build()))
                .build();
        when(bookDtoMapper.map(bookDto)).thenReturn(book);
        when(bookRepository.save(bookToBeSaved)).thenReturn(bookFromDao);
        when(authorService.saveIfNotExists(author.build())).thenReturn(authorFromService);
        when(genreService.saveIfNotExists(genre.build())).thenReturn(genreFromService);
        when(bookRepository.findById(bookFromDao.getId())).thenReturn(Optional.of(book));

        //when
        var result = sut.save(bookDto);

        //then
        assertEquals(bookFromDao.getId(), result);
        verify(bookRepository, times(1)).updateAuthor(bookFromDao.getId(), authorFromService);
    }

    @Test
    @DisplayName("Должен получать книгу по id")
    void shouldGetBookById() {
        //given
        var author = Author.builder()
                .id(2L)
                .fullName("fullName")
                .build();
        var genre = Genre.builder()
                .id(3L)
                .genreName("genreName")
                .build();
        var expectedBook = Book.builder()
                .id(1L)
                .name("name")
                .author(author)
                .genres(List.of(genre))
                .build();
        var bookDto = mock(BookDto.class);
        when(bookRepository.findById(expectedBook.getId())).thenReturn(Optional.of(expectedBook));
        when(bookDtoMapper.mapToDto(expectedBook)).thenReturn(bookDto);

        //when
        var actualBook = sut.getById(expectedBook.getId());

        //then
        assertThat(actualBook)
                .usingRecursiveComparison()
                .isEqualTo(Optional.of(bookDto));
    }

    @Test
    @DisplayName("Должен получать все книги")
    void shouldGetAllBooks() {
        //given
        var author = Author.builder()
                .id(2L)
                .fullName("fullName")
                .build();
        var genre = Genre.builder()
                .id(3L)
                .genreName("genreName")
                .build();
        var expectedBook = Book.builder()
                .id(1L)
                .name("name")
                .author(author)
                .genres(List.of(genre))
                .build();
        var bookDto = mock(BookDto.class);
        when(bookRepository.findAll()).thenReturn(List.of(expectedBook));
        when(bookDtoMapper.mapToDto(expectedBook)).thenReturn(bookDto);

        //when
        var actualBooks = sut.getAll();

        //then
        assertThat(actualBooks)
                .containsAll(List.of(bookDto));
    }

    @ParameterizedTest
    @MethodSource("getAuthorsAndNamesCombinations")
    @DisplayName("Должен обновлять книгу, у которой нет жанров")
    void shouldUpdateBook_whenNoGenres(String name, String newName, Author author, Author newAuthor) {
        //given

        var bookDto = mock(BookDto.class);
        var genre = Genre.builder()
                .id(3L)
                .genreName("genreName")
                .build();
        var bookBefore = Book.builder()
                .id(1L)
                .name(name)
                .author(author)
                .genres(List.of(genre))
                .build();
        var expectedBook = Book.builder()
                .id(1L)
                .name(newName)
                .author(newAuthor)
                .genres(List.of())
                .build();
        when(bookDtoMapper.map(bookDto)).thenReturn(expectedBook);
        when(bookRepository.findById(expectedBook.getId())).thenReturn(Optional.of(bookBefore));
        if (nonNull(newAuthor)) {
            when(authorService.saveIfNotExists(newAuthor)).thenReturn(newAuthor);
        }

        //when
        sut.updateById(bookDto);

        //then
        verify(bookRepository, times(1)).save(eq(expectedBook));
    }

    private static Stream<Arguments> getAuthorsAndNamesCombinations() {
        var author1 = Author.builder()
                .id(2L)
                .fullName("fullName")
                .build();
        var author2 = Author.builder()
                .id(4L)
                .fullName("newFullName")
                .build();
        return Stream.of(
                Arguments.of("name", null, author1, null),
                Arguments.of(null, "name", null, author1),
                Arguments.of("name", "newName", author1, author2)
        );
    }

    @Test
    @DisplayName("Должен обновлять книгу c новыми жанрами")
    void shouldUpdateBook_whenNewGenres() {
        //given
        var bookDto = mock(BookDto.class);
        var oldGenre = Genre.builder()
                .id(3L)
                .genreName("genreName")
                .build();
        var newGenre = Genre.builder()
                .id(4L)
                .genreName("newGenreName")
                .build();
        var bookBefore = Book.builder()
                .id(1L)
                .name("name")
                .author(null)
                .genres(List.of(oldGenre))
                .build();
        var expectedBook = Book.builder()
                .id(1L)
                .name("name")
                .author(null)
                .genres(List.of(newGenre))
                .build();
        when(bookDtoMapper.map(bookDto)).thenReturn(expectedBook);
        when(bookRepository.findById(expectedBook.getId())).thenReturn(Optional.of(bookBefore));

        //when
        sut.updateById(bookDto);

        //then
        verify(bookRepository, times(1)).save(expectedBook);
    }

    @Test
    @DisplayName("Должен удалять книгу по id")
    void shouldDeleteBookById() {
        //given
        var book = Book.builder().id(1L).build();

        //when
        sut.deleteById(book.getId());

        //then
        verify(bookRepository, times(1)).deleteById(book.getId());
    }

}