package com.example.spring31.controller;

import com.example.spring31.domain.dto.AuthorDto;
import com.example.spring31.domain.dto.BookDto;
import com.example.spring31.domain.dto.GenreDto;
import com.example.spring31.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @RequestMapping(value = "/books", method = RequestMethod.POST)
    public ResponseEntity<String> addBook(@RequestParam String bookName,
                                          @RequestParam(required = false) String authorFullName,
                                          @RequestParam(required = false) String... genreNames) {

        long savedBookId = bookService.save(createBookDto(bookName, authorFullName, genreNames));
        return ResponseEntity.ok().body("Saved book id : " + savedBookId);
    }

    @RequestMapping(value = "/books/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getBookById(@PathVariable long id) {
        Optional<BookDto> foundBook = bookService.getById(id);
        return foundBook.map(bookDto -> ResponseEntity.ok().body(MessageFormat.format("Book by id {0}: {1}", id, bookDto))).orElseGet(() -> ResponseEntity.ok().body(MessageFormat.format("Book by id {0} not found", id)));
    }

    @RequestMapping(value = "/books", method = RequestMethod.GET)
    public ResponseEntity<String> getAllBooks() {
        var foundBooks = bookService.getAll();
        return ResponseEntity.ok().body("Found books are :\n" + foundBooks.stream()
                .map(BookDto::toString)
                .collect(Collectors.joining("\n")));
    }

    @RequestMapping(value = "/books/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> updateBook(@PathVariable long id,
                                             @RequestParam(required = false) String bookName,
                                             @RequestParam(required = false) String authorFullName,
                                             @RequestParam(required = false) String... genreNames) {
        var bookToBeUpdated = createBookDto(bookName, authorFullName, genreNames);
        bookToBeUpdated.setId(id);
        bookService.updateById(bookToBeUpdated);
        return ResponseEntity.ok().body(MessageFormat.format("Book by id {0} is updated", id));
    }

    @RequestMapping(value = "/books/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteBookById(@PathVariable long id) {
        bookService.deleteById(id);
        return ResponseEntity.ok().body(MessageFormat.format("Book by id {0} is deleted", id));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleNotFound(Exception e) {
        return ResponseEntity.badRequest().body("Error occurred: " + e.getMessage());
    }

    private BookDto createBookDto(String bookName,
                                  String authorFullName,
                                  String... genreNames) {
        var genres = nonNull(genreNames) ? Arrays.stream(genreNames)
                .map(genreName -> GenreDto.builder()
                        .genreName(genreName)
                        .build())
                .toList() : null;
        var author = nonNull(authorFullName) ? AuthorDto.builder()
                .fullName(authorFullName)
                .build() : null;
        return BookDto.builder()
                .name(bookName)
                .author(author)
                .genres(genres)
                .build();
    }
}
