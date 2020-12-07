CREATE SCHEMA IF NOT EXISTS archive;
SET SCHEMA archive;

CREATE TABLE IF NOT EXISTS docs (
    docid UUID PRIMARY KEY,
    title varchar(100) NOT NULL,
    person varchar(100) NOT NULL,
    filesize int NOT NULL
);

INSERT INTO docs (docid, title, person, filesize) VALUES ('56e84b17-b32c-49b9-8497-63833144c210', 'Steuererklärung', 'Andjelko Perisic', 4096);