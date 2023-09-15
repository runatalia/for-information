--liquibase formatted sql
--changeset Natalia:create-table-users
CREATE TABLE users
(
    id                  bigserial PRIMARY KEY,
    email               varchar(64)          NOT NULL UNIQUE,
    image               varchar(128)         NOT NULL,
    birthdate           DATE                 NOT NULL,
    id_city             INTEGER REFERENCES city (Id) ON DELETE SET NULL ON UPDATE CASCADE,
    experience          enum_experience_type NOT NULL,
    himself_description text,
    is_blocked          boolean              NOT NULL DEFAULT false
);

