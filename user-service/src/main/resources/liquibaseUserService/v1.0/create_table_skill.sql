--liquibase formatted sql
--changeset Natalia:create-table-skill
CREATE TABLE skill
(
    id   smallserial PRIMARY KEY,
    name varchar(128) NOT NULL UNIQUE
);
