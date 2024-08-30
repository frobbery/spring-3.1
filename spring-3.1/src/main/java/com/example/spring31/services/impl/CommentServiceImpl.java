package com.example.spring31.services.impl;


import com.example.spring31.domain.dto.CommentDto;
import com.example.spring31.domain.entity.Comment;
import com.example.spring31.mapper.Mapper;
import com.example.spring31.repository.BookRepository;
import com.example.spring31.repository.CommentRepository;
import com.example.spring31.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final Mapper<Comment, CommentDto> commentDtoMapper;

    @Override
    @Transactional
    public long addToBook(CommentDto commentDto) {
        var comment = commentDtoMapper.map(commentDto);
        var savedComment = commentRepository.save(Comment.builder().text(comment.getText()).build());
        var book = bookRepository.findById(comment.getBook().getId());
        book.ifPresent(value -> commentRepository.updateBookById(savedComment.getId(), value));
        return savedComment.getId();
    }

    @Override
    public Optional<CommentDto> getById(long id) {
        return commentRepository.findById(id).map(commentDtoMapper::mapToDto);
    }

    @Override
    public List<CommentDto> getAllOfBookWithId(long bookId) {
        return commentRepository.findAllByBookId(bookId).stream()
                .map(commentDtoMapper::mapToDto).toList();
    }

    @Override
    public void updateTextById(long id, String newText) {
        commentRepository.updateTextById(id, newText);
    }

    @Override
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }
}
