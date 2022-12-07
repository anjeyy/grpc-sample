package com.github.anjeyy.infrastructure.config;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ObjectMapperConfig implements InitializingBean {

    private final ObjectMapper objectMapper;

    @Override
    public void afterPropertiesSet() throws Exception {
        objectMapper.setSerializationInclusion(Include.NON_NULL);
    }
}
