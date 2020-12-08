package com.github.anjeyy.util;

import com.github.anjeyy.api.dao.entity.Document;
import com.github.anjeyy.api.dto.model.DocumentDto;
import java.util.UUID;

public final class TestdataSupplier {

    private TestdataSupplier() {
        throw new UnsupportedOperationException("Utility Class - no instantiation needed.");
    }

    public static DocumentDto createDocumentDtoTestdata() {
        return DocumentDto.builder()
                          .docid(UUID.randomUUID())
                          .title("JUnit Test")
                          .person("githubUser")
                          .filesize(2048)
                          .build();
    }

    public static Document createDocumentTestdata() {
        Document document = new Document();
        document.setDocid(UUID.randomUUID());
        document.setTitle("JUnit Test");
        document.setPerson("githubUser");
        document.setFilesize(2048);

        return document;
    }
}
