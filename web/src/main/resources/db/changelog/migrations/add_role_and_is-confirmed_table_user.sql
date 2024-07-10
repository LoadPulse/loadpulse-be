-- liquibase formatted sql

-- changeset duchuy:1720436482221-1
ALTER TABLE users
    ADD is_confirmed BOOLEAN;
ALTER TABLE users
    ADD role VARCHAR(255);

