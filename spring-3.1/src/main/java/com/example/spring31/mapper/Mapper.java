package com.example.spring31.mapper;

public interface Mapper<T, E> {

    E mapToDto(T objectToMap);

    T map(E objectToMap);
}
