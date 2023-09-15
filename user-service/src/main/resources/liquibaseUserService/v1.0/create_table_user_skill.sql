--liquibase formatted sql
--changeset Natalia:bridge_user_skill

CREATE TABLE user_skill
(
    id_user  BIGINT REFERENCES users (id) ON DELETE CASCADE,
    id_skill SMALLINT REFERENCES skill (id) ON DELETE CASCADE,
    PRIMARY KEY (id_user, id_skill)
);
