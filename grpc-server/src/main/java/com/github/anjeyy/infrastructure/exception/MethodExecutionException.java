package com.github.anjeyy.infrastructure.exception;


public class MethodExecutionException extends RuntimeException {

    public MethodExecutionException(String msg, Throwable e) {
        super(msg, e);
    }
}
