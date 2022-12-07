package com.github.anjeyy.client.api.dto.mapper;

import com.github.anjeyy.client.api.dto.model.DocumentDto;
import com.github.anjeyy.proto.document.DocumentResponse;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DocumentDtoMapper {
    @Mapping(target = "docid", source = "docId")
    @Mapping(target = "person", source = "person")
    DocumentDto mapFromGrpcDocumentResponse(DocumentResponse document);

    List<DocumentDto> mapFromGrpcDocumentResponseList(List<DocumentResponse> document);
}
