package com.github.anjeyy.infrastructure.validation.test;

import com.github.anjeyy.infrastructure.validation.GrpcConstraint;
import com.github.anjeyy.infrastructure.validation.GrpcConstraintValidator;
import com.github.anjeyy.proto.document.DocumentRequest;

@GrpcConstraint
public class Document2RequestValidator implements GrpcConstraintValidator<DocumentRequest> {

    @Override
    public boolean isValid(DocumentRequest request) {

//        UUID.fromString(request.getDocId());

        return request.getDocId().length() > 10;
    }

}
