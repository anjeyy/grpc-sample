package com.github.anjeyy.api.mapper;

import com.github.anjeyy.api.dao.entity.Document;
import com.github.anjeyy.api.dto.mapper.DocumentDtoMapper;
import com.github.anjeyy.api.dto.mapper.DocumentDtoMapperImpl;
import com.github.anjeyy.api.dto.model.DocumentDto;
import com.github.anjeyy.util.TestdataSupplier;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class DocumentDtoMapperTest {

    private final DocumentDtoMapper uut = new DocumentDtoMapperImpl();


    @Test
    void map_validDocument_returnsValidDocumentDto() {
        // prepare - given
        Document document = TestdataSupplier.createDocumentTestdata();

        // execute - when
        DocumentDto result = uut.mapFromDocument(document);

        // verify - then
        Assertions.assertThat(result)
                  .usingRecursiveComparison()
                  .ignoringFields("docid")
                  .isEqualTo(TestdataSupplier.createDocumentDtoTestdata());
    }

    @Test
    void map_documentWithTitleNull_returnsValidDocumentDto() {
        // prepare
        Document document = TestdataSupplier.createDocumentTestdata();
        document.setTitle(null);
        DocumentDto expectedResult = DocumentDto.builder()
                                                .docid(UUID.randomUUID())
                                                .title(null)
                                                .person("githubUser")
                                                .filesize(2048)
                                                .build();

        // execute
        DocumentDto result = uut.mapFromDocument(document);

        // verify
        Assertions.assertThat(result)
                  .usingRecursiveComparison()
                  .ignoringFields("docid")
                  .isEqualTo(expectedResult);
    }
}
