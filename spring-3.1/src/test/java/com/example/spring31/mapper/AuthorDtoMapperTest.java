package com.example.spring31.mapper;

import com.example.spring31.domain.dto.AuthorDto;
import com.example.spring31.domain.entity.Author;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AuthorDtoMapperTest {

    private final AuthorDtoMapper sut = new AuthorDtoMapper();

    @Test
    void shouldMapToDto() {
        //given
        var objectToMap = Author.builder()
                .id(1L)
                .fullName("fullName")
                .build();
        var expectedDto = AuthorDto.builder()
                .id(objectToMap.getId())
                .fullName(objectToMap.getFullName())
                .build();

        //when
        var mappedObject = sut.mapToDto(objectToMap);

        //then
        assertThat(mappedObject)
                .usingRecursiveComparison()
                .isEqualTo(expectedDto);
    }

    @Test
    void shouldMapToEntity() {
        //given
        var objectToMap = AuthorDto.builder()
                .id(1L)
                .fullName("fullName")
                .build();
        var expectedEntity = Author.builder()
                .id(objectToMap.getId())
                .fullName(objectToMap.getFullName())
                .build();

        //when
        var mappedObject = sut.map(objectToMap);

        //then
        assertThat(mappedObject)
                .usingRecursiveComparison()
                .isEqualTo(expectedEntity);
    }
}