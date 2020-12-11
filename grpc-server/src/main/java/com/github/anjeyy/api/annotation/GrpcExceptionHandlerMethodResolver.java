package com.github.anjeyy.api.annotation;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GrpcExceptionHandlerMethodResolver {

    private final Map<Class<? extends Throwable>, Method> mappedMethods = new HashMap<>(16);

    
}
