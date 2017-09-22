CREATE TABLE agwmail$user (
    id integer not null primary key autoincrement,
    username varchar(255) not null unique,
    email varchar(255) not null unique,
    password varchar(255) not null
);

CREATE TABLE agwmail$friend (
    id integer not null primary key autoincrement,
    user_id integer not null,
    friend_id integer not null,
    friend_name varchar(255) not null,
    unique (user_id, friend_id)
);

CREATE TABLE agwmail$mail (
    id integer not null primary key autoincrement,
    subject varchar(255) not null,
    from_user_id integer not null,
    from_user_name varchar(255) not null,
    to_user_id integer not null,
    to_user_name varchar(255) not null,
    date date not null,
    text long blob not null
);

CREATE TABLE agwmail$inbox (
    id integer not null primary key autoincrement,
    mail_id integer not null unique,
    subject varchar(255) not null,
    from_user_id integer not null,
    from_user_name varchar(255) not null,
    to_user_id integer not null,
    to_user_name varchar(255) not null,
    date date not null
);

CREATE TABLE agwmail$preinbox (
    id integer not null primary key autoincrement,
    subject varchar(255) not null,
    from_user_id integer not null,
    to_user_id integer not null,
    date date not null,
    text long blob not null
);

CREATE TABLE agwmail$context (
    id integer not null primary key autoincrement,
    mail_id integer not null unique,
    subject varchar(255) not null,
    stream long blob not null
);

CREATE TABLE agwmail$function (
    id integer not null primary key autoincrement,
    modifier integer,
    class_id integer,
    name varchar(255) not null,
    source long blob not null,
    binary long blob not null,
    unique(class_id, name)
);

