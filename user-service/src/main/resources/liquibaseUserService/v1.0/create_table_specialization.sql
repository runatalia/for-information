--liquibase formatted sql
--changeset Natalia:create-table-specialization

CREATE TABLE specialization
(
    id   smallserial PRIMARY KEY,
    name varchar(128) NOT NULL UNIQUE
);
