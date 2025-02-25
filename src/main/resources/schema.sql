create table if not exists films (
    id bigint generated by default as identity primary key,
    name nvarchar(255),
    description nvarchar(255),
    release_date date,
    duration integer,
    genre bigint,
    mpa bigint
);
create table if not exists users (
    id bigint generated by default as identity primary key,
    name nvarchar(255),
    email nvarchar(255),
    login nvarchar(255),
    birthday date
);
create table if not exists friendship (
    user_id bigint,
    friend_id bigint,
    accepted boolean
);
create table if not exists likes (
    user_id bigint,
    film_id bigint
);
create table if not exists genres (
    id bigint primary key,
    name nvarchar(255)
);
create table if not exists mpa (
    id bigint primary key,
    name nvarchar(255),
    description nvarchar(255)
);


