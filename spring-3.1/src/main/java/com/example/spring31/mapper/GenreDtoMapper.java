package com.example.spring31.mapper;

import com.example.spring31.domain.dto.GenreDto;
import com.example.spring31.domain.entity.Genre;
import org.springframework.stereotype.Component;

@Component
public class GenreDtoMapper implements Mapper<Genre, GenreDto> {
    @Override
    public GenreDto mapToDto(Genre objectToMap) {
        return GenreDto.builder()
                .id(objectToMap.getId())
                .genreName(objectToMap.getGenreName())
                .build();
    }

    @Override
    public Genre map(GenreDto objectToMap) {
        return Genre.builder()
                .id(objectToMap.getId())
                .genreName(objectToMap.getGenreName())
                .build();
    }
}
