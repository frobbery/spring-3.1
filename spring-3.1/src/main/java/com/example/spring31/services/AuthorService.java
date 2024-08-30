package com.example.spring31.services;

import com.example.spring31.domain.entity.Author;

public interface AuthorService {

    Author saveIfNotExists(Author author);
}
