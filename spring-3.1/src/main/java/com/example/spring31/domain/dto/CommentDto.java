package com.example.spring31.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentDto {

    private final Long id;

    private final String text;

    private final BookDto book;

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", fullName='" + text + '\'' +
                '}';
    }
}
