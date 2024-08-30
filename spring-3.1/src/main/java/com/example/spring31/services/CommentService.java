package com.example.spring31.services;

import com.example.spring31.domain.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    long addToBook(CommentDto comment);

    Optional<CommentDto> getById(long id);

    List<CommentDto> getAllOfBookWithId(long bookId);

    void updateTextById(long id, String newText);

    void deleteById(long id);
}
