package com.example.spring31.repository;

import com.example.spring31.config.YamlPropertySourceFactory;
import com.example.spring31.domain.entity.Author;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Dao для работы с авторами должно:")
@DataJpaTest
@TestPropertySource(value = "/application.yml", factory = YamlPropertySourceFactory.class)
@Sql(value = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository sut;

    @Test
    @DisplayName("Возврашать автора по полному имени")
    void shouldGetByFullname() {
        //given
        var expectedAuthor = Author.builder()
                .id(1L)
                .fullName("Pushkin")
                .build();

        //when
        var actualAuthor = sut.findByFullName(expectedAuthor.getFullName());

        //then
        assertThat(actualAuthor)
                .isNotEmpty()
                .contains(expectedAuthor);
    }

    @Test
    @DisplayName("Сохранять автора")
    void shouldSaveNewAuthor() {
        //given
        var expectedAuthor = Author.builder()
                .id(3L)
                .fullName("New")
                .build();

        //when
        var actualAuthor = sut.save(expectedAuthor);

        //then
        assertThat(actualAuthor)
                .usingRecursiveComparison()
                .isEqualTo(expectedAuthor);
    }

}