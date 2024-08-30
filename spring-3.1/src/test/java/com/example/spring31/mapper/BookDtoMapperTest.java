package com.example.spring31.mapper;

import com.example.spring31.domain.dto.AuthorDto;
import com.example.spring31.domain.dto.BookDto;
import com.example.spring31.domain.dto.GenreDto;
import com.example.spring31.domain.entity.Author;
import com.example.spring31.domain.entity.Book;
import com.example.spring31.domain.entity.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookDtoMapperTest {

    @Mock
    private Mapper<Author, AuthorDto> authorDtoMapper;

    @Mock
    private Mapper<Genre, GenreDto> genreDtoMapper;

    private BookDtoMapper sut;

    @BeforeEach
    void initSut() {
        sut = new BookDtoMapper(authorDtoMapper, genreDtoMapper);
    }

    @Test
    void shouldMapToDto() {
        //given
        var author = mock(Author.class);
        var genre = mock(Genre.class);
        var objectToMap = Book.builder()
                .id(1L)
                .name("name")
                .author(author)
                .genres(List.of(genre))
                .build();
        var authorDto = mock(AuthorDto.class);
        var genreDto = mock(GenreDto.class);
        var expectedObject = BookDto.builder()
                .id(objectToMap.getId())
                .name(objectToMap.getName())
                .author(authorDto)
                .genres(List.of(genreDto))
                .build();
        when(authorDtoMapper.mapToDto(author)).thenReturn(authorDto);
        when(genreDtoMapper.mapToDto(genre)).thenReturn(genreDto);

        //when
        var mappedObject = sut.mapToDto(objectToMap);

        //then
        assertThat(mappedObject)
                .usingRecursiveComparison()
                .isEqualTo(expectedObject);
    }

    @Test
    void shouldMapToEntity() {
        //given
        var authorDto = mock(AuthorDto.class);
        var genreDto = mock(GenreDto.class);
        var objectToMap = BookDto.builder()
                .id(1L)
                .name("name")
                .author(authorDto)
                .genres(List.of(genreDto))
                .build();
        var author = mock(Author.class);
        var genre = mock(Genre.class);
        var expectedEntity = Book.builder()
                .id(objectToMap.getId())
                .name(objectToMap.getName())
                .author(author)
                .genres(List.of(genre))
                .build();
        when(authorDtoMapper.map(authorDto)).thenReturn(author);
        when(genreDtoMapper.map(genreDto)).thenReturn(genre);

        //when
        var mappedObject = sut.map(objectToMap);

        //then
        assertThat(mappedObject)
                .usingRecursiveComparison()
                .isEqualTo(expectedEntity);
    }
}