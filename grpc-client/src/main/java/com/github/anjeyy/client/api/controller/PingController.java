package com.github.anjeyy.client.api.controller;

import com.github.anjeyy.client.api.service.GrpcHelloServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/ping")
public class PingController {

    private final GrpcHelloServiceClient grpcHelloServiceClient;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createCustomPrometheusMetrics() {
        String response = grpcHelloServiceClient.sayHello();
        return ResponseEntity.ok(response);
    }
}
