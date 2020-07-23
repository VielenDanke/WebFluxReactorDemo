create table users (
    id bigserial primary key,
    username varchar(64),
    password varchar(64),
    role varchar(64)
)