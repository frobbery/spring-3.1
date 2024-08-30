package com.example.spring31.controller;

import com.example.spring31.domain.dto.BookDto;
import com.example.spring31.domain.dto.CommentDto;
import com.example.spring31.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.MessageFormat;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @RequestMapping(value = "/books/{id}/comments", method = RequestMethod.POST)
    public ResponseEntity<String> addCommentToBook(@PathVariable long id,
                                                   @RequestParam String commentText) {
        long savedCommentId = commentService.addToBook(CommentDto.builder()
                .book(BookDto.builder()
                        .id(id)
                        .build())
                .text(commentText)
                .build());
        return ResponseEntity.ok().body("Saved comment id : " + savedCommentId);
    }

    @RequestMapping(value = "/books/{id}/comments", method = RequestMethod.GET)
    public ResponseEntity<String> getAllCommentsByBookId(@PathVariable long id) {
        var comments = commentService.getAllOfBookWithId(id);
        return ResponseEntity.ok().body("Comments are : " + comments.stream()
                .map(CommentDto::toString)
                .collect(Collectors.joining("\n")));
    }

    @RequestMapping(value = "/books/{id}/comments/{commentId}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteCommentById(@PathVariable long id,
                                                    @PathVariable long commentId) {
        commentService.deleteById(commentId);
        return ResponseEntity.ok().body(MessageFormat.format("Comment by id {0} is deleted", commentId));
    }

    @RequestMapping(value = "/books/{id}/comments/{commentId}", method = RequestMethod.PUT)
    public ResponseEntity<String> updateCommentTextById(@PathVariable long id,
                                      @PathVariable long commentId,
                                      @RequestParam String newText) {
        commentService.updateTextById(commentId, newText);
        return ResponseEntity.ok().body(MessageFormat.format("Comment by id {0} is updated", commentId));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleNotFound(Exception e) {
        return ResponseEntity.badRequest().body("Error occurred: " + e.getMessage());
    }
}
