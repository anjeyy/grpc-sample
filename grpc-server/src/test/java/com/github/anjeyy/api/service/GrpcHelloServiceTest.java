package com.github.anjeyy.api.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.anjeyy.proto.simple.HelloRequest;
import com.github.anjeyy.proto.simple.HelloResponse;
import io.grpc.internal.testing.StreamRecorder;
import java.util.concurrent.TimeUnit;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GrpcHelloServiceTest {

    private final GrpcHelloService uut = new GrpcHelloService();

    @Test
    void hello() throws Exception {
        // given
        HelloRequest testInput = HelloRequest.newBuilder().setFirstName("Andjelko").setLastName("Perisic").build();
        StreamRecorder<HelloResponse> actualResponse = StreamRecorder.create();
        HelloResponse expectedResponse = HelloResponse
            .newBuilder()
            .setGreeting("Hello Perisic, Andjelko. This is my first grpc response with spring boot.")
            .build();

        // when
        uut.hello(testInput, actualResponse);

        // then
        boolean responseReceived = actualResponse.awaitCompletion(5, TimeUnit.SECONDS);
        if (!responseReceived) {
            Assertions.fail("Timeout - no response received.");
        }
        assertThat(actualResponse.getError()).isNull();
        assertThat(actualResponse.getValues())
            .isNotEmpty()
            .hasSize(1)
            .element(0)
            .extracting(HelloResponse::getGreeting)
            .isEqualTo(expectedResponse.getGreeting());
    }
}
