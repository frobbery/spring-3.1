package com.example.spring31.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

import static java.util.Objects.nonNull;

@Builder
@Data
public class BookDto {

    private Long id;

    private String name;

    private AuthorDto author;

    private List<GenreDto> genres;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Book{ id=").append(id).append(", name='").append(name).append('\'');
        if (nonNull(author)) {
            sb.append(", author=").append(author);
        }
        if (nonNull(genres)) {
            sb.append(", genres=").append(genres);
        }
        sb.append("}");
        return sb.toString();
    }
}
