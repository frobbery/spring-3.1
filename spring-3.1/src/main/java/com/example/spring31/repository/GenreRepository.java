package com.example.spring31.repository;

import com.example.spring31.domain.entity.Genre;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GenreRepository extends CrudRepository<Genre, Long> {
    Optional<Genre> findByGenreName(String genreName);
}
