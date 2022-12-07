package com.github.anjeyy.api.service;

import com.github.anjeyy.api.dto.mapper.DocumentResponseMapper;
import com.github.anjeyy.api.dto.model.DocumentDto;
import com.github.anjeyy.proto.document.DocumentRequest;
import com.github.anjeyy.proto.document.DocumentResponse;
import com.github.anjeyy.proto.document.DocumentResponseList;
import com.github.anjeyy.proto.document.DocumentServiceGrpc;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@GrpcService
@RequiredArgsConstructor
public class GrpcDocumentService extends DocumentServiceGrpc.DocumentServiceImplBase {

    private final DocumentService documentService;
    private final DocumentResponseMapper documentResponseMapper;


    @Override
    public void getDocumentById(DocumentRequest request, StreamObserver<DocumentResponse> responseObserver) {

        final UUID id = UUID.fromString(request.getDocId());
        DocumentDto foundDocument = documentService.findDocumentById(id);
        DocumentResponse documentResponse = documentResponseMapper.mapFromDocument(foundDocument);

        responseObserver.onNext(documentResponse);
        responseObserver.onCompleted();
    }


    @Override
    public void getAllDocumentsAsList(Empty request, StreamObserver<DocumentResponseList> responseObserver) {

        List<DocumentDto> foundAllDocuments = documentService.findAllDocuments();

        Function<List<DocumentResponse>, DocumentResponseList> wrapDocResponse =
            docResp -> DocumentResponseList.newBuilder()
                                           .addAllDocumentResponse(docResp)
                                           .build();
        DocumentResponseList response =
            foundAllDocuments.stream()
                             .map(documentResponseMapper::mapFromDocument)
                             .collect(Collectors.collectingAndThen(Collectors.toList(), wrapDocResponse));

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllDocuments(Empty request, StreamObserver<DocumentResponse> responseObserver) {
        List<DocumentDto> foundAllDocuments = documentService.findAllDocuments();

        foundAllDocuments.stream()
                         .map(this::simulateHeavyOperation)
                         .forEach(responseObserver::onNext);

        responseObserver.onCompleted();
    }

    private DocumentResponse simulateHeavyOperation(DocumentDto documentDto) {
        try {
            Thread.sleep(1_000L);
        } catch (InterruptedException e) {
            throw new IllegalThreadStateException("A Thread was interupted: " + e);
        }
        return documentResponseMapper.mapFromDocument(documentDto);
    }


}
