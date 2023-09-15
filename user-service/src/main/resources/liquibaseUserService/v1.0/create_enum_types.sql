--liquibase formatted sql
--changeset Polina:create-enum-types

CREATE TYPE enum_level_type AS ENUM ('SECONDARY', 'SPECIALIZEDSECONDARY', 'INCOMPLETEHIGHER', 'HIGHER',
    'BACHELORDEGREE', 'MASTERDEGREE');
CREATE CAST (character varying as enum_level_type) WITH INOUT AS IMPLICIT;

CREATE TYPE enum_experience_type AS ENUM ('NOEXPERIENCE','LESS1YEAR', 'YEAR1', 'YEAR2TO3', 'YEAR4TO6', 'OVER6YEAR');
CREATE CAST (character varying as enum_experience_type) WITH INOUT AS IMPLICIT;

