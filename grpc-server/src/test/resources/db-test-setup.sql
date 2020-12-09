CREATE TABLE IF NOT EXISTS docs (
    docid UUID PRIMARY KEY,
    title varchar(100) NOT NULL,
    person varchar(100) NOT NULL,
    filesize int NOT NULL
);

INSERT INTO docs (docid, title, person, filesize) VALUES ('29e84b17-b32c-49b9-8497-63833144c210', 'TEST_TITEL', 'integration-test', 4096);