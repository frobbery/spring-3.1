package com.example.spring31.mapper;

import com.example.spring31.domain.dto.AuthorDto;
import com.example.spring31.domain.entity.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorDtoMapper implements Mapper<Author, AuthorDto> {
    @Override
    public AuthorDto mapToDto(Author objectToMap) {
        return AuthorDto.builder()
                .id(objectToMap.getId())
                .fullName(objectToMap.getFullName())
                .build();
    }

    @Override
    public Author map(AuthorDto objectToMap) {
        return Author.builder()
                .id(objectToMap.getId())
                .fullName(objectToMap.getFullName())
                .build();
    }
}
