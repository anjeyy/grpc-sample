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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Sql(scripts = "classpath:db-test-setup.sql")
class DocumentRepositoryIntTest {

    @Autowired
    private DocumentRepository uut;

    @Test
    void findAll_success() {
        // prepare
        List<Document> toBeExpected = createExpectedDocument();

        // execute
        List<Document> result = uut.findAll();

        // verify
        Assertions.assertThat(result)
                  .isNotEmpty()
                  .hasSize(2)
                  .usingRecursiveComparison()
                  .isEqualTo(createExpectedDocument());
    }

    // ###################
    // ### H E L P E R ###
    // ###################

    private List<Document> createExpectedDocument() {
        Document first = new Document();
        first.setDocid(UUID.fromString("29e84b17-b32c-49b9-8497-63833144c210"));
        first.setTitle("TEST_TITEL");
        first.setPerson("integration-test");
        first.setFilesize(4096);

        Document second = new Document();
        second.setDocid(UUID.fromString("39e84b17-b32c-49b9-8497-63833144c210"));
        second.setTitle("TEST_TITEL_TWO");
        second.setPerson("integration-test");
        second.setFilesize(2048);

        return List.of(first, second);
    }
}
