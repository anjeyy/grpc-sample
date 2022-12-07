package com.github.anjeyy.api.service;

import com.github.anjeyy.api.dao.entity.Document;
import com.github.anjeyy.api.dao.repository.DocumentRepository;
import com.github.anjeyy.api.dto.mapper.DocumentDtoMapper;
import com.github.anjeyy.api.dto.model.DocumentDto;
import com.github.anjeyy.infrastructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentDtoMapper documentDtoMapper;


    public List<DocumentDto> findAllDocuments() {
        List<Document> document = documentRepository.findAll();

        return document.stream()
                       .map(documentDtoMapper::mapFromDocument)
                       .toList();
    }

    public DocumentDto findDocumentById(UUID id) {
        Document document = documentRepository.findById(id)
                                              .orElseThrow(() -> new ResourceNotFoundException(
                                                  String.format("Document NOT found with ID %s", id))
                                              );
        return documentDtoMapper.mapFromDocument(document);
    }
}
