-- liquibase formatted sql

-- changeset duchuy:1719376901143-1
CREATE TABLE users
(
    id           UUID         NOT NULL,
    created_at   TIMESTAMP WITHOUT TIME ZONE,
    updated_at   TIMESTAMP WITHOUT TIME ZONE,
    deleted_at   TIMESTAMP WITHOUT TIME ZONE,
    email        VARCHAR(255) NOT NULL,
    password     VARCHAR(255) NOT NULL,
    confirmed_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

-- changeset duchuy:1719376901143-2
ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

