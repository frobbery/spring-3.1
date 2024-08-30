package com.example.spring31.repository;

import com.example.spring31.config.YamlPropertySourceFactory;
import com.example.spring31.domain.entity.Genre;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Dao для работы с жанрами должно:")
@DataJpaTest
@TestPropertySource(value = "/application.yml", factory = YamlPropertySourceFactory.class)
@Sql(value = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class GenreRepositoryTest {

    @Autowired
    private GenreRepository sut;

    @Test
    @DisplayName("Возврашать жанр по названию")
    void shouldGetGenreByName() {
        //given
        var expectedGenre = sut.save(Genre.builder()
                .genreName("new")
                .build());

        //when
        var actualGenre = sut.findByGenreName(expectedGenre.getGenreName());

        //then
        assertThat(actualGenre)
                .isNotEmpty()
                .contains(expectedGenre);
    }
}