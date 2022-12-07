package com.github.anjeyy.api.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.anjeyy.proto.document.DocumentResponse;
import com.github.anjeyy.proto.document.DocumentServiceGrpc;
import com.google.protobuf.Empty;
import io.grpc.internal.testing.StreamRecorder;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext
@SpringBootTest(
    properties = {
        "grpc.server.inProcessName=test", "grpc.server.port=-1", "grpc.client.inProcess.address=in-process:test",
    }
)
class GrpcDocumentServiceTest {

    @GrpcClient("inProcess")
    private DocumentServiceGrpc.DocumentServiceBlockingStub documentServiceBlockingStub;

    @GrpcClient("inProcess")
    private DocumentServiceGrpc.DocumentServiceStub documentServiceNonBlockingStub;

    @Test
    @DirtiesContext
    void getAllDocuments_blockingStub() {
        //given
        Empty testInput = Empty.newBuilder().build();

        // when
        Iterator<DocumentResponse> actualResult = documentServiceBlockingStub.getAllDocuments(testInput);
        Spliterator<DocumentResponse> spliterator = Spliterators.spliteratorUnknownSize(
            actualResult,
            Spliterator.ORDERED
        );
        List<DocumentResponse> actualResultList = StreamSupport.stream(spliterator, false).collect(Collectors.toList());

        // then
        assertThat(actualResultList).isNotNull().hasSize(2).usingRecursiveComparison().isEqualTo(createExpected());
    }

    @Test
    @DirtiesContext
    void getAllDocuments_nonBlockingStub() throws Exception {
        //given
        Empty testInput = Empty.newBuilder().build();

        // when
        StreamRecorder<DocumentResponse> actualResponse = StreamRecorder.create();
        documentServiceNonBlockingStub.getAllDocuments(testInput, actualResponse);
        boolean responseReceived = actualResponse.awaitCompletion(3, TimeUnit.SECONDS);
        if (!responseReceived) {
            Assertions.fail("Timeout - no response received.");
        }

        // then
        assertThat(actualResponse.getError()).isNull();
        assertThat(actualResponse.getValues())
            .isNotEmpty()
            .hasSize(2)
            .usingRecursiveComparison()
            .isEqualTo(createExpected());
    }

    // ###################
    // ### H E L P E R ###
    // ###################

    private List<DocumentResponse> createExpected() {
        DocumentResponse first = DocumentResponse
            .newBuilder()
            .setDocId("29e84b17-b32c-49b9-8497-63833144c210")
            .setTitle("TEST_TITEL")
            .setPerson("integration-test")
            .setFilesize(4096)
            .build();
        DocumentResponse second = DocumentResponse
            .newBuilder()
            .setDocId("39e84b17-b32c-49b9-8497-63833144c210")
            .setTitle("TEST_TITEL_TWO")
            .setPerson("integration-test")
            .setFilesize(2048)
            .build();

        return List.of(first, second);
    }
}
