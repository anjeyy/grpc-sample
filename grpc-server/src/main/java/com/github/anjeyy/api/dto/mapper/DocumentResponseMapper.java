package com.github.anjeyy.api.dto.mapper;

import com.github.anjeyy.api.dto.model.DocumentDto;
import com.github.anjeyy.proto.document.DocumentResponse;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface DocumentResponseMapper {
    List<DocumentResponse> mapFromDocumentList(List<DocumentDto> document);

    @Mapping(target = "docId", source = "docid", qualifiedByName = "uuidToString")
    @Mapping(target = "person", source = "person")
    DocumentResponse mapFromDocument(DocumentDto document);

    @Named("uuidToString")
    static String uuidToString(UUID id) {
        return Optional.ofNullable(id).map(UUID::toString).orElse("");
    }
}
