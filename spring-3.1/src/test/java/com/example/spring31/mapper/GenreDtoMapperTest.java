package com.example.spring31.mapper;

import com.example.spring31.domain.dto.GenreDto;
import com.example.spring31.domain.entity.Genre;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class GenreDtoMapperTest {

    private final GenreDtoMapper sut = new GenreDtoMapper();

    @Test
    void mapToDto() {
        //given
        var objectToMap = Genre.builder()
                .id(1L)
                .genreName("genreName")
                .build();
        var expectedDto = GenreDto.builder()
                .id(objectToMap.getId())
                .genreName(objectToMap.getGenreName())
                .build();

        //when
        var mappedObject = sut.mapToDto(objectToMap);

        //then
        assertThat(mappedObject)
                .usingRecursiveComparison()
                .isEqualTo(expectedDto);
    }

    @Test
    void map() {
        //given
        var objectToMap = GenreDto.builder()
                .id(1L)
                .genreName("genreName")
                .build();
        var expectedEntity = Genre.builder()
                .id(objectToMap.getId())
                .genreName(objectToMap.getGenreName())
                .build();

        //when
        var mappedObject = sut.map(objectToMap);

        //then
        assertThat(mappedObject)
                .usingRecursiveComparison()
                .isEqualTo(expectedEntity);
    }
}