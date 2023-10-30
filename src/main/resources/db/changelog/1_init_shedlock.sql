--liquibase formatted sql

--changeset v.petrova:1 endDelimiter:/

create table shedlock
(
    name            varchar(64)  not null,
    lock_until      timestamp    not null,
    locked_at       timestamp    not null,
    locked_by       varchar(255) not null,
    PRIMARY KEY (name)
);