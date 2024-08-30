package com.example.spring31.services;


import com.example.spring31.domain.entity.Genre;

public interface GenreService {

    Genre saveIfNotExists(Genre genre);
}
