package com.github.anjeyy.api.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.anjeyy.api.dto.model.DocumentDto;
import com.github.anjeyy.api.service.DocumentService;
import com.github.anjeyy.infrastructure.config.GlobalRestAdvice;
import com.github.anjeyy.infrastructure.exception.ResourceNotFoundException;
import com.github.anjeyy.util.TestdataSupplier;
import java.util.Collections;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class DocumentControllerTest {

    @InjectMocks
    private DocumentController uut;

    @Mock
    private DocumentService documentService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(uut).setControllerAdvice(new GlobalRestAdvice()).build();

        objectMapper = new ObjectMapper();
    }

    @Test
    void getAllDocuments_successfulCall() throws Exception {
        // prepare
        UUID randomId = UUID.randomUUID();
        DocumentDto mockedResult = TestdataSupplier.createDocumentDtoTestdata();

        // execute
        given(documentService.findAllDocuments()).willReturn(Collections.singletonList(mockedResult));

        // verify
        mockMvc
            .perform(MockMvcRequestBuilders.get("/documents"))
            .andExpect(status().isOk())
            .andExpect(content().string(objectMapper.writeValueAsString(Collections.singletonList(mockedResult))));
        verify(documentService, times(1)).findAllDocuments();
        verify(documentService, never()).findDocumentById(randomId);
    }

    @Test
    void getSingleDocuments_successfulCall() throws Exception {
        // prepare
        UUID randomId = UUID.randomUUID();
        DocumentDto mockedResult = DocumentDto
            .builder()
            .docid(randomId)
            .title("JUnit Test")
            .person("Andjelko")
            .filesize(1024)
            .build();

        // execute
        given(documentService.findDocumentById(randomId)).willReturn(mockedResult);

        // verify
        mockMvc
            .perform(MockMvcRequestBuilders.get("/documents/" + randomId.toString()))
            .andExpect(status().isOk())
            .andExpect(content().string(objectMapper.writeValueAsString(mockedResult)));
        verify(documentService, times(1)).findDocumentById(randomId);
        verify(documentService, never()).findAllDocuments();
    }

    @Test
    void expectGlobalJdbiCustomExceptionHandling_whenErrorThrown() throws Exception {
        // prepare
        UUID randomId = UUID.randomUUID();
        BDDMockito
            .willThrow(new ResourceNotFoundException("Simulate SQL Error"))
            .given(documentService)
            .findDocumentById(randomId);

        // execute & verify
        mockMvc
            .perform(MockMvcRequestBuilders.get("/documents/" + randomId.toString()))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Simulate SQL Error"));
    }
}
