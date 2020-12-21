CREATE TABLE IF NOT EXISTS docs (
    docid UUID PRIMARY KEY,
    title varchar(100) NOT NULL,
    person varchar(100) NOT NULL,
    filesize int NOT NULL
);

DELETE FROM docs;
INSERT INTO docs (docid, title, person, filesize) VALUES ('29e84b17-b32c-49b9-8497-63833144c210', 'TEST_TITEL', 'integration-test', 4096), ('39e84b17-b32c-49b9-8497-63833144c210', 'TEST_TITEL_TWO', 'integration-test', 2048);