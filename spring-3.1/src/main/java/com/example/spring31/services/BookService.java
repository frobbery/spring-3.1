package com.example.spring31.services;

import com.example.spring31.domain.dto.BookDto;

import java.util.List;
import java.util.Optional;

public interface BookService {

    long save(BookDto book);

    Optional<BookDto> getById(long id);

    List<BookDto> getAll();

    void updateById(BookDto book);

    void deleteById(long id);
}
