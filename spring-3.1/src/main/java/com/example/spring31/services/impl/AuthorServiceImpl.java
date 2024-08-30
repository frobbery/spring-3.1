package com.example.spring31.services.impl;


import com.example.spring31.domain.entity.Author;
import com.example.spring31.repository.AuthorRepository;
import com.example.spring31.services.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    public Author saveIfNotExists(Author author) {
        return authorRepository.findByFullName(author.getFullName()).orElse(authorRepository.save(author));
    }
}
