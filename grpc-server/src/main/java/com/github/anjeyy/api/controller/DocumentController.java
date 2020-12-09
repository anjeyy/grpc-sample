package com.github.anjeyy.api.controller;

import com.github.anjeyy.api.dto.model.DocumentDto;
import com.github.anjeyy.api.service.DocumentService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/documents")
public class DocumentController {

    private final DocumentService documentService;


    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DocumentDto> createCustomPrometheusMetrics(@PathVariable UUID id) {
        DocumentDto response = documentService.findDocumentById(id);
        return ResponseEntity.ok(response);
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DocumentDto>> createCustomPrometheusMetrics() {
        List<DocumentDto> response = documentService.findAllDocuments();
        return ResponseEntity.ok(response);
    }
}