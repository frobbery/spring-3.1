package com.example.spring31.services.impl;


import com.example.spring31.domain.dto.BookDto;
import com.example.spring31.domain.entity.Author;
import com.example.spring31.domain.entity.Book;
import com.example.spring31.domain.entity.Genre;
import com.example.spring31.mapper.Mapper;
import com.example.spring31.repository.BookRepository;
import com.example.spring31.services.AuthorService;
import com.example.spring31.services.BookService;
import com.example.spring31.services.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final AuthorService authorService;

    private final GenreService genreService;

    private final Mapper<Book, BookDto> bookDtoMapper;

    @Override
    @Transactional
    public long save(BookDto bookDto) {
        var book = bookDtoMapper.map(bookDto);
        var bookId = bookRepository.save(Book.builder().name(book.getName()).build()).getId();
        var author = authorService.saveIfNotExists(book.getAuthor());
        bookRepository.updateAuthor(bookId, author);
        saveGenres(bookId, book.getGenres());
        return bookId;
    }

    private void saveGenres(long bookId, List<Genre> genres) {
        if (!isEmpty(genres)) {
            var book = bookRepository.findById(bookId);
            if (book.isEmpty()) {
                throw new IllegalArgumentException("There is no book with such id");
            }
            var updateBook = book.get();
            updateBook.setGenres(genres.stream()
                    .map(genreService::saveIfNotExists)
                    .collect(Collectors.toList()));
            bookRepository.save(updateBook);
        }
    }

    @Override
    public Optional<BookDto> getById(long bookId) {
        return bookRepository.findById(bookId).map(bookDtoMapper::mapToDto);
    }

    @Override
    public List<BookDto> getAll() {
        return bookRepository.findAll().stream().map(bookDtoMapper::mapToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateById(BookDto newBookDto) {
        var newBook = bookDtoMapper.map(newBookDto);
        var oldBookOptional = bookRepository.findById(newBook.getId());
        if (oldBookOptional.isEmpty()) {
            throw new IllegalArgumentException("There is no book with such id");
        }
        var oldBook = oldBookOptional.get();
        if ((nonNull(oldBook.getName()) && !oldBook.getName().equals(newBook.getName())) ||
                (isNull(oldBook.getName()) && nonNull(newBook.getName()))) {
            oldBook.setName(newBook.getName());
        }
        if ((nonNull(oldBook.getAuthor()) &&
                (isNull(newBook.getAuthor()) || !oldBook.getAuthor().getFullName().equals(newBook.getAuthor().getFullName())))
                || (isNull(oldBook.getAuthor()) && nonNull(newBook.getAuthor()))) {
            Author author;
            if (isNull(newBook.getAuthor())) {
                author = null;
            } else {
                author = authorService.saveIfNotExists(newBook.getAuthor());
            }
            oldBook.setAuthor(author);
        }
        if (!(oldBook.getGenres().size() == newBook.getGenres().size() && new HashSet<>(oldBook.getGenres()).containsAll(newBook.getGenres()))) {
            oldBook.setGenres(newBook.getGenres());
        }
        bookRepository.save(oldBook);
    }

    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }
}
