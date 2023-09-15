--liquibase formatted sql
--changeset Natalia:create-table-institution
CREATE TABLE institution
(
    id         smallserial PRIMARY KEY,
    short_name varchar(64)  NOT NULL,
    full_name  varchar(128) NOT NULL UNIQUE
);


