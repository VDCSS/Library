package com.example.demo.mapper;

import com.example.demo.dto.PersonDTO;
import com.example.demo.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PersonMapper {
    @Mapping(target = "password", ignore = true)
    PersonDTO toDTO(Person p);

    Person toEntity(PersonDTO dto);
}
