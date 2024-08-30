package com.example.spring31.services.impl;



import com.example.spring31.domain.entity.Genre;
import com.example.spring31.repository.GenreRepository;
import com.example.spring31.services.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    public Genre saveIfNotExists(Genre genre) {
        return genreRepository.findByGenreName(genre.getGenreName()).orElse(genreRepository.save(genre));
    }
}
