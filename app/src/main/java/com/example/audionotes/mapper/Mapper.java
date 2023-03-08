package com.example.audionotes.mapper;

public interface Mapper <E, D>{
    E getEntityToDto(D dto);
    D getDtoToEntity(E entity);
}
