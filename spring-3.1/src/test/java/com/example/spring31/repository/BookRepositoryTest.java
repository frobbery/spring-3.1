package com.example.spring31.repository;

import com.example.spring31.config.YamlPropertySourceFactory;
import com.example.spring31.domain.entity.Author;
import com.example.spring31.domain.entity.Book;
import com.example.spring31.domain.entity.Genre;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Dao для работы с книгами должно:")
@DataJpaTest
@TestPropertySource(value = "/application.yml", factory = YamlPropertySourceFactory.class)
@Sql(value = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class BookRepositoryTest {

    @Autowired
    private BookRepository sut;

    @Test
    @DisplayName("Находить книгу по id")
    void shouldFindBookById() {
        //given
        var expectedBook = Book.builder()
                .id(1L)
                .name("Regular adventure novel")
                .author(Author.builder()
                        .id(1L)
                        .fullName("Pushkin")
                        .build())
                .build();

        //when
        var actualBook = sut.findById(expectedBook.getId());

        //then
        assertThat(actualBook)
                .isPresent()
                .usingRecursiveComparison()
                .ignoringFields("value.genres", "value.comments")
                .isEqualTo(Optional.of(expectedBook));
    }

    @Test
    @DisplayName("Обновлять название книги по id")
    @Transactional
    @Rollback
    void shouldUpdateBookNameById() {
        //given
        var expectedBook = Book.builder()
                .id(1L)
                .name("New")
                .build();

        //when
        sut.updateNameById(expectedBook.getId(), expectedBook.getName());

        //then
        assertThat(sut.findById(expectedBook.getId()))
                .isPresent()
                .usingRecursiveComparison()
                .ignoringFields("value.author", "value.genres", "value.comments")
                .isEqualTo(Optional.of(expectedBook));
    }

    @Test
    @DisplayName("Удалять книгу по id")
    @Transactional
    @Rollback
    void shouldDeleteBookById() {
        //given
        var bookId = 3L;

        //when
        sut.deleteById(bookId);

        //then
        assertThat(sut.findById(bookId))
                .isEmpty();
    }

    @Test
    @DisplayName("Обновлять автора книги")
    @Transactional
    @Rollback
    void shouldUpdateBookAuthor() {
        //given
        var newAuthor = Author.builder()
                .id(2L)
                .fullName("Rubina")
                .build();
        var expectedBook = Book.builder()
                .id(1L)
                .name("Regular adventure novel")
                .author(newAuthor)
                .genres(List.of(Genre.builder()
                        .id(1L)
                        .genreName("Adventure")
                        .build()))
                .build();

        //when
        sut.updateAuthor(expectedBook.getId(), newAuthor);

        //then
        assertThat(sut.findById(expectedBook.getId()))
                .isPresent()
                .usingRecursiveComparison()
                .ignoringFields("value.comments", "value.genres")
                .isEqualTo(Optional.of(expectedBook));
    }
}