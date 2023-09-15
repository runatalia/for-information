--liquibase formatted sql
--changeset Natalia:create-table-city
CREATE TABLE city
(
    id         smallserial PRIMARY KEY,
    name       varchar(64)                                                          NOT NULL,
    id_country SMALLINT REFERENCES country (id) ON UPDATE CASCADE ON DELETE CASCADE NOT NULL

);
