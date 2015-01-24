# --- !Ups //#A

CREATE TABLE users (
    id long PRIMARY KEY,
    username text,
    name text,
    lastName text,
    email text,
    phone text,
    activated bool,
    confirmed bool,
    password text);

CREATE TABLE activations(
    userId long PRIMARY KEY,
    secret text);

# --- !Downs
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS activations;
