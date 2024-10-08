package com.example.spring31.services.author;

import com.example.spring31.domain.entity.Author;
import com.example.spring31.repository.AuthorRepository;
import com.example.spring31.services.impl.AuthorServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Сервис для работы с авторами должен:")
@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl sut;

    @Test
    @DisplayName("Сохранять несуществующего автора")
    void shouldSaveNotExistingAuthor() {
        //given
        var author = mock(Author.class);
        when(authorRepository.findByFullName(author.getFullName())).thenReturn(Optional.empty());
        when(authorRepository.save(author)).thenReturn(author);

        //when
        var result = sut.saveIfNotExists(author);

        //then
        assertEquals(author, result);
    }

    @Test
    @DisplayName("Возвращать существующего автора")
    void shouldReturnExistingAuthor() {
        //given
        var author = mock(Author.class);
        when(authorRepository.findByFullName(author.getFullName())).thenReturn(Optional.of(author));

        //when
        var result = sut.saveIfNotExists(author);

        //then
        assertEquals(author, result);
    }
}