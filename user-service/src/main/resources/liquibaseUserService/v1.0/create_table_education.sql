--liquibase formatted sql
--changeset Natalia:create-table-education
CREATE TABLE education
(
    id                smallserial PRIMARY KEY,
    id_user           BIGINT REFERENCES users (id) ON UPDATE CASCADE ON DELETE CASCADE NOT NULL,
    level             enum_level_type                                                  NOT NULL,
    id_institution    SMALLINT REFERENCES institution (id) ON UPDATE CASCADE ON DELETE CASCADE,
    faculty           varchar(64)                                                      NOT NULL,
    id_specialization SMALLINT REFERENCES specialization (id) ON DELETE SET NULL ON UPDATE CASCADE,
    year_of_ending    smallInt                                                         NOT NULL CHECK (year_of_ending > 1960)

);
