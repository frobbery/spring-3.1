package com.example.spring31.repository;

import com.example.spring31.domain.entity.Author;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AuthorRepository extends CrudRepository<Author, Long> {

    Optional<Author> findByFullName(String fullName);
}
