CREATE SCHEMA IF NOT EXISTS archive;
SET SCHEMA archive;

CREATE TABLE IF NOT EXISTS docs (
    docid UUID PRIMARY KEY,
    title varchar(100) NOT NULL,
    person varchar(100) NOT NULL,
    filesize int NOT NULL
);

INSERT INTO docs (docid, title, person, filesize)
VALUES ('56e84b17-b32c-49b9-8497-63833144c210', 'Steuererklärung', 'Andjelko Perisic', 4096),
('67e84b17-b32c-49b9-8497-63833144c210', 'Steuererklärung', 'Kasim Edebali', 2048),
('78e84b17-b32c-49b9-8497-63833144c210', 'Steuererklärung', 'Jogi Löw', 1024),
('89e84b17-b32c-49b9-8497-63833144c210', 'Steuererklärung', 'Angela Merkel', 512),
('90e84b17-b32c-49b9-8497-63833144c210', 'Steuererklärung', 'Björn Werner', 8192);