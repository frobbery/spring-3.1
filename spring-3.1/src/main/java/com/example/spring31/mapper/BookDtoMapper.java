package com.example.spring31.mapper;

import com.example.spring31.domain.dto.AuthorDto;
import com.example.spring31.domain.dto.BookDto;
import com.example.spring31.domain.dto.GenreDto;
import com.example.spring31.domain.entity.Author;
import com.example.spring31.domain.entity.Book;
import com.example.spring31.domain.entity.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookDtoMapper implements Mapper<Book, BookDto> {

    private final Mapper<Author, AuthorDto> authorDtoMapper;

    private final Mapper<Genre, GenreDto> genreDtoMapper;

    @Override
    public BookDto mapToDto(Book objectToMap) {
        return BookDto.builder()
                .id(objectToMap.getId())
                .name(objectToMap.getName())
                .author(authorDtoMapper.mapToDto(objectToMap.getAuthor()))
                .genres(objectToMap.getGenres().stream()
                        .map(genreDtoMapper::mapToDto).toList())
                .build();
    }

    @Override
    public Book map(BookDto objectToMap) {
        return Book.builder()
                .id(objectToMap.getId())
                .name(objectToMap.getName())
                .author(authorDtoMapper.map(objectToMap.getAuthor()))
                .genres(objectToMap.getGenres().stream().map(genreDtoMapper::map).toList())
                .build();
    }
}
