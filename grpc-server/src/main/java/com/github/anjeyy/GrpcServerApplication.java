package com.github.anjeyy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

@SpringBootApplication
public class GrpcServerApplication {

    public static void main(String[] args) {

        // startup actuator since
        SpringApplication app = new SpringApplication(GrpcServerApplication.class);
        app.setApplicationStartup(new BufferingApplicationStartup(1500));
        app.run(args);
    }

}
