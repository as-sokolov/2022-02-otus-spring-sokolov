DROP TABLE IF EXISTS AUTHORS;
CREATE TABLE AUTHORS(ID BIGINT PRIMARY KEY, NAME VARCHAR(255));
DROP TABLE IF EXISTS GENRES;
CREATE TABLE GENRES(ID BIGINT PRIMARY KEY, NAME VARCHAR(255));
ALTER TABLE GENRES ADD CONSTRAINT NAME_UNIQUE UNIQUE(NAME);
DROP TABLE IF EXISTS BOOKS;
CREATE TABLE BOOKS(ID BIGINT PRIMARY KEY, NAME VARCHAR(255), GENRE_ID BIGINT references GENRES (id));
DROP TABLE IF EXISTS BOOK_AUTHOR;
CREATE TABLE BOOKS_AUTHORS(BOOK_ID BIGINT references BOOKS (id), AUTHOR_ID BIGINT references AUTHORS (id));
DROP TABLE IF EXISTS COMMENTS;
CREATE TABLE COMMENTS(ID BIGINT, TEXT VARCHAR(255), BOOK_ID BIGINT references BOOKS (id));

DROP SEQUENCE IF EXISTS AUTHORS_SEQ;
CREATE SEQUENCE AUTHORS_SEQ  START WITH 1 INCREMENT BY 1;

DROP SEQUENCE IF EXISTS COMMENTS_SEQ;
CREATE SEQUENCE COMMENTS_SEQ  START WITH 1 INCREMENT BY 1;

DROP SEQUENCE IF EXISTS GENRES_SEQ;
CREATE SEQUENCE GENRES_SEQ  START WITH 1 INCREMENT BY 1;

DROP SEQUENCE IF EXISTS BOOKS_SEQ;
CREATE SEQUENCE BOOKS_SEQ  START WITH 1 INCREMENT BY 1;
