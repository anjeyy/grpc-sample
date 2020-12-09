package com.github.anjeyy.api.repository;

import com.github.anjeyy.api.dao.entity.Document;
import com.github.anjeyy.api.dao.repository.DocumentRepository;
import java.util.List;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class DocumentRepositoryIntTest {

    @Autowired
    private DocumentRepository uut;

    @Test
    void findAll_success() {
        // prepare
        Document toBeExpected = createExpectedDocument();

        // execute
        List<Document> result = uut.findAll();

        // verify
        result.forEach(doc -> Assertions.assertThat(doc).usingRecursiveComparison().isEqualTo(toBeExpected));
    }

    // ###################
    // ### H E L P E R ###
    // ###################

    private Document createExpectedDocument() {
        Document toBeExpected = new Document();
        toBeExpected.setDocid(UUID.fromString("29e84b17-b32c-49b9-8497-63833144c210"));
        toBeExpected.setTitle("TEST_TITEL");
        toBeExpected.setPerson("integration-test");
        toBeExpected.setFilesize(4096);

        return toBeExpected;
    }
}
