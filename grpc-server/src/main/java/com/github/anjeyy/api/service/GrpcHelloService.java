package com.github.anjeyy.api.service;

import com.github.anjeyy.proto.simple.HelloRequest;
import com.github.anjeyy.proto.simple.HelloResponse;
import com.github.anjeyy.proto.simple.HelloServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class GrpcHelloService extends HelloServiceGrpc.HelloServiceImplBase {

    @Override
    public void hello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        String response = String.format(
            "Hello %s, %s. This is my first grpc response with spring boot.",
            request.getLastName(),
            request.getFirstName()
        );
        HelloResponse helloResponse = HelloResponse.newBuilder().setGreeting(response).build();

        responseObserver.onNext(helloResponse);
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<HelloRequest> helloInputStream(StreamObserver<HelloResponse> responseObserver) {
        return super.helloInputStream(responseObserver);
    }

    @Override
    public void helloOutputStream(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        super.helloOutputStream(request, responseObserver);
    }

    @Override
    public StreamObserver<HelloRequest> helloBiStream(StreamObserver<HelloResponse> responseObserver) {
        return super.helloBiStream(responseObserver);
    }
}
