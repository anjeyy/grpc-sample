package com.github.anjeyy.client.api.service;

import com.github.anjeyy.proto.document.DocumentRequest;
import com.github.anjeyy.proto.document.DocumentResponse;
import com.github.anjeyy.proto.document.DocumentResponseList;
import com.github.anjeyy.proto.document.DocumentServiceGrpc;
import com.google.protobuf.Empty;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GrpcDocumentServiceClient {

    @GrpcClient("document-service")
    private DocumentServiceGrpc.DocumentServiceBlockingStub documentServiceBlockingStub;


    public DocumentResponse getDocumentsWithId(UUID id) {
        DocumentRequest request = DocumentRequest.newBuilder().setDocId(id.toString()).build();
        log.info("Document requesting: " + request);

        DocumentResponse response = documentServiceBlockingStub.getDocumentById(request);
        log.info("Document received: " + response);
        return response;
    }

    public DocumentResponseList getAllDocumentsAsList() {
        Empty request = Empty.newBuilder().build();
        log.info("All documents requesting.");

        DocumentResponseList response = documentServiceBlockingStub.getAllDocumentsAsList(request);
        log.info("Document received: " + response);
        return response;
    }
}
