package com.github.anjeyy.api.dto.mapper;

import com.github.anjeyy.api.dao.entity.Document;
import com.github.anjeyy.api.dto.model.DocumentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DocumentDtoMapper {

    @Mapping(target = "person", source = "person")
    DocumentDto mapFromDocument(Document document);
}
