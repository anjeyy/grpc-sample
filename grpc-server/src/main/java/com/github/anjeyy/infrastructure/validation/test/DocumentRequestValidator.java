package com.github.anjeyy.infrastructure.validation.test;

import com.github.anjeyy.infrastructure.validation.GrpcConstraint;
import com.github.anjeyy.infrastructure.validation.GrpcConstraintValidator;
import com.github.anjeyy.proto.document.DocumentRequest;

@GrpcConstraint
public class DocumentRequestValidator implements GrpcConstraintValidator<DocumentRequest> {

    @Override
    public boolean isValid(DocumentRequest request) {

        return request.getDocId().length() > 5;
    }

}
