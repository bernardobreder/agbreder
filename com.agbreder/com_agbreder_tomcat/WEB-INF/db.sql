CREATE TABLE agbreder$user (
    id integer not null primary key autoincrement,
    username varchar(255) not null unique,
    email varchar(255) not null unique,
    password varchar(255) not null
); 

INSERT INTO agbreder$user (username, email, password) VALUES ('bbreder', 'bernardobreder@gmail.com', 'bbreder');

CREATE TABLE agbreder$mail (
    id integer not null primary key autoincrement,
    parent_id integer,
    subject varchar(255) not null,
    from_user_id integer not null,
    to_user_id integer not null,
    date date not null,
    text long blob not null
);

CREATE TABLE agbreder$source (
    id integer not null primary key autoincrement,
    user_id integer not null,
    date date not null,
    name varchar(255) not null,
    source long blob not null,
    binary long blob not null
);