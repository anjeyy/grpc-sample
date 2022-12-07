package com.github.anjeyy.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.github.anjeyy.api.dao.entity.Document;
import com.github.anjeyy.api.dao.repository.DocumentRepository;
import com.github.anjeyy.api.dto.mapper.DocumentDtoMapper;
import com.github.anjeyy.api.dto.model.DocumentDto;
import com.github.anjeyy.infrastructure.exception.ResourceNotFoundException;
import com.github.anjeyy.util.TestdataSupplier;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private DocumentDtoMapper documentToDocumentDto;

    @InjectMocks
    private DocumentService uut;

    @Test
    void findAllDocuments_success() {
        // prepare
        Document mockedDbResult = TestdataSupplier.createDocumentTestdata();
        DocumentDto mockedMapResult = TestdataSupplier.createDocumentDtoTestdata();
        given(documentRepository.findAll()).willReturn(Collections.singletonList(mockedDbResult));
        given(documentToDocumentDto.mapFromDocument(mockedDbResult)).willReturn(mockedMapResult);

        // execute
        List<DocumentDto> result = uut.findAllDocuments();

        // verify
        result.forEach(doc -> assertEquals(mockedMapResult, doc));
        verify(documentRepository, times(1)).findAll();
        verify(documentToDocumentDto, times(1)).mapFromDocument(mockedDbResult);
    }

    @Test
    void emptyResult_findAllDocuments_success() {
        // prepare
        given(documentRepository.findAll()).willReturn(Collections.emptyList());

        // execute
        List<DocumentDto> result = uut.findAllDocuments();

        // verify
        assertEquals(Collections.emptyList(), result);
        verify(documentRepository, times(1)).findAll();
        verify(documentToDocumentDto, never()).mapFromDocument(any(Document.class));
    }

    @Test
    void findDocumentById_IdWithoutEntity_throwsResourceNotFoundException() {
        // prepare
        Document mockedDbResult = TestdataSupplier.createDocumentTestdata();
        DocumentDto mockedMapResult = TestdataSupplier.createDocumentDtoTestdata();
        given(documentRepository.findById(mockedDbResult.getDocid())).willReturn(Optional.empty());

        // execute
        ThrowingCallable result = () -> uut.findDocumentById(mockedDbResult.getDocid());

        // verify
        Assertions.assertThatThrownBy(result).isInstanceOf(ResourceNotFoundException.class);
        verify(documentRepository, times(1)).findById(mockedDbResult.getDocid());
        verify(documentToDocumentDto, never()).mapFromDocument(mockedDbResult);
    }

    @Test
    void findDocumentById_IdWithFoundEntity_returnsValidDocumentDto() {
        // prepare
        Document mockedDbResult = TestdataSupplier.createDocumentTestdata();
        DocumentDto mockedMapResult = TestdataSupplier.createDocumentDtoTestdata();
        given(documentRepository.findById(mockedDbResult.getDocid())).willReturn(Optional.of(mockedDbResult));
        given(documentToDocumentDto.mapFromDocument(mockedDbResult)).willReturn(mockedMapResult);

        // execute
        DocumentDto result = uut.findDocumentById(mockedDbResult.getDocid());

        // verify
        Assertions.assertThat(result).isEqualTo(mockedMapResult);
        verify(documentRepository, times(1)).findById(mockedDbResult.getDocid());
        verify(documentToDocumentDto, times(1)).mapFromDocument(mockedDbResult);
    }
}
