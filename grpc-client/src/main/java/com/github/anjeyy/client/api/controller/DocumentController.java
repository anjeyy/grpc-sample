package com.github.anjeyy.client.api.controller;

import com.github.anjeyy.client.api.dto.mapper.DocumentDtoMapper;
import com.github.anjeyy.client.api.dto.model.DocumentDto;
import com.github.anjeyy.client.api.service.GrpcDocumentServiceClient;
import com.github.anjeyy.proto.document.DocumentResponse;
import com.github.anjeyy.proto.document.DocumentResponseList;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/documents")
public class DocumentController {

    private final GrpcDocumentServiceClient documentServiceClient;
    private final DocumentDtoMapper documentDtoMapper;


    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DocumentDto> getDocumentsById(@PathVariable UUID id) {

        DocumentResponse foundDocument = documentServiceClient.getDocumentsWithId(id);
        DocumentDto response = documentDtoMapper.mapFromGrpcDocumentResponse(foundDocument);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/wrapper", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DocumentDto>> getAllDocumentsAsList() {
        DocumentResponseList allDocuments = documentServiceClient.getAllDocumentsAsList();
        List<DocumentDto> response =
            documentDtoMapper.mapFromGrpcDocumentResponseList(allDocuments.getDocumentResponseList());

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/stream", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DocumentDto>> getAllDocumentsFromStream(
    ) {
        Iterator<DocumentResponse> allDocuments = documentServiceClient.getAllDocumentsAsIterator();
        List<DocumentResponse> allDocs = Lists.newArrayList(allDocuments);
        List<DocumentDto> response =
            documentDtoMapper.mapFromGrpcDocumentResponseList(allDocs);

        return ResponseEntity.ok(response);
    }


    @GetMapping(path = "/flux", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<DocumentDto> getAllDocumentsWithFlux() {
        Flux<DocumentResponse> rawResponse = documentServiceClient.getAllDocumentsAsStream();
        Flux<DocumentDto> response = rawResponse.map(documentDtoMapper::mapFromGrpcDocumentResponse);

        return response;
    }


}