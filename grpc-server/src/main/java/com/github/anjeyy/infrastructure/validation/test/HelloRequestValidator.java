package com.github.anjeyy.infrastructure.validation.test;

import com.github.anjeyy.infrastructure.validation.GrpcConstraint;
import com.github.anjeyy.infrastructure.validation.GrpcConstraintValidator;
import com.github.anjeyy.proto.simple.HelloRequest;

@GrpcConstraint
public class HelloRequestValidator implements GrpcConstraintValidator<HelloRequest> {

    @Override
    public boolean isValid(HelloRequest request) {

        return !request.getFirstName().equals("Andjelko");
    }
}
