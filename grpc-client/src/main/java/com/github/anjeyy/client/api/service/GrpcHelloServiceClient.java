package com.github.anjeyy.client.api.service;

import com.github.anjeyy.proto.simple.HelloRequest;
import com.github.anjeyy.proto.simple.HelloResponse;
import com.github.anjeyy.proto.simple.HelloServiceGrpc;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GrpcHelloServiceClient {

    @GrpcClient("hello-service")
    private HelloServiceGrpc.HelloServiceBlockingStub helloServiceBlockingStub;

    public String sayHello() {
        HelloRequest request = HelloRequest.newBuilder().setFirstName("Andjelko").setLastName("Perisic").build();
        log.info("Client request: " + request);

        HelloResponse response = helloServiceBlockingStub.hello(request);
        log.info("gRPC response received: " + response);
        return response.getGreeting();
    }
}
