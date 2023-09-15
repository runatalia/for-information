--liquibase formatted sql
--changeset Natalia:create-table-country
CREATE TABLE country
(
    id   smallserial PRIMARY KEY,
    name varchar(64) NOT NULL UNIQUE
);
