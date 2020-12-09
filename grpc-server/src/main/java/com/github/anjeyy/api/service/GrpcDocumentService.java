package com.github.anjeyy.api.service;

import com.github.anjeyy.api.dto.mapper.DocumentResponseMapper;
import com.github.anjeyy.api.dto.model.DocumentDto;
import com.github.anjeyy.proto.document.DocumentRequest;
import com.github.anjeyy.proto.document.DocumentResponse;
import com.github.anjeyy.proto.document.DocumentResponseList;
import com.github.anjeyy.proto.document.DocumentServiceGrpc;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class GrpcDocumentService extends DocumentServiceGrpc.DocumentServiceImplBase {

    private final DocumentService documentService;
    private final DocumentResponseMapper documentResponseMapper;


    @Override
    public void getDocumentById(DocumentRequest request, StreamObserver<DocumentResponse> responseObserver) {
        final UUID id = UUID.fromString(request.getDocId());    //TODO handle illegalArgument
        DocumentDto foundDocument = documentService.findDocumentById(id);   //TODO handle resource not found
        DocumentResponse documentResponse = documentResponseMapper.mapFromDocument(foundDocument);

        responseObserver.onNext(documentResponse);
        responseObserver.onCompleted();
    }


    @Override
    public void getAllDocumentsAsList(Empty request, StreamObserver<DocumentResponseList> responseObserver) {

        List<DocumentDto> foundAllDocuments = documentService.findAllDocuments();
        List<DocumentResponse> documentResponse = documentResponseMapper.mapFromDocumentList(foundAllDocuments);
        DocumentResponseList response =
            DocumentResponseList.newBuilder()
                                .addAllDocumentResponse(documentResponse)
                                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
