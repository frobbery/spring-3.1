package com.example.spring31.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenreDto {

    private final Long id;

    private final String genreName;

    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", genreName='" + genreName + '\'' +
                '}';
    }
}
