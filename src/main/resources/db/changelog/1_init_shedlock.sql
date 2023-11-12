--liquibase formatted sql

--changeset v.petrova:1 endDelimiter:/

create table shedlock
(
    name                varchar(64) not null,
    lock_until          timestamp not null,
    locked_at           timestamp not null,
    locked_by           varchar(255) not null,
    PRIMARY KEY (name)
);

create table users
(
    id                  int not null generated always as identity,
    name                varchar not null,
    username            varchar not null,
    password            varchar not null,
    primary key (id)
);

create table refresh_tokens
(
    id                  uuid not null,
    access_token        uuid not null,
    expires_at          timestamp not null,
    user_id             int not null references users on delete cascade,
    primary key (id)
);