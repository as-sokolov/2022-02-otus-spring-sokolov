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
DROP TABLE IF EXISTS USERS;
CREATE TABLE USERS(ID BIGINT, login VARCHAR(255), hash VARCHAR(255), enabled BOOLEAN, accountnonexpired BOOLEAN, credentialsnonexpired BOOLEAN, nonlocked
BOOLEAN, role VARCHAR(255)
);

DROP SEQUENCE IF EXISTS AUTHORS_SEQ;
CREATE SEQUENCE AUTHORS_SEQ  START WITH 1 INCREMENT BY 1;

DROP SEQUENCE IF EXISTS COMMENTS_SEQ;
CREATE SEQUENCE COMMENTS_SEQ  START WITH 1 INCREMENT BY 1;

DROP SEQUENCE IF EXISTS GENRES_SEQ;
CREATE SEQUENCE GENRES_SEQ  START WITH 1 INCREMENT BY 1;

DROP SEQUENCE IF EXISTS BOOKS_SEQ;
CREATE SEQUENCE BOOKS_SEQ  START WITH 1 INCREMENT BY 1;

DROP SEQUENCE IF EXISTS USERS_SEQ;
CREATE SEQUENCE USERS_SEQ  START WITH 1 INCREMENT BY 1;

create table if not exists acl_sid(
	id bigserial not null primary key  AUTO_INCREMENT,
	principal boolean not null,
	sid varchar(100) not null,
	constraint unique_uk_1 unique(sid,principal)
);

create table if not exists acl_class(
	id bigserial not null primary key  AUTO_INCREMENT,
	class varchar(100) not null,
	constraint unique_uk_2 unique(class)
);
-- commit
create table if not exists acl_object_identity(
	id bigserial primary key  AUTO_INCREMENT,
	object_id_class bigint not null,
	object_id_identity bigint not null,
	parent_object bigint,
	owner_sid bigint,
	entries_inheriting boolean not null,
	constraint unique_uk_3 unique(object_id_class,object_id_identity),
	constraint foreign_fk_1 foreign key(parent_object)references acl_object_identity(id),
	constraint foreign_fk_2 foreign key(object_id_class)references acl_class(id),
	constraint foreign_fk_3 foreign key(owner_sid)references acl_sid(id)
);
-- commit
create table if not exists acl_entry(
	id bigserial primary key  AUTO_INCREMENT,
	acl_object_identity bigint not null,
	ace_order int not null,
	sid bigint not null,
	mask integer not null,
	granting boolean not null,
	audit_success boolean not null,
	audit_failure boolean not null,
	constraint unique_uk_4 unique(acl_object_identity,ace_order),
	constraint foreign_fk_4 foreign key(acl_object_identity) references acl_object_identity(id),
	constraint foreign_fk_5 foreign key(sid) references acl_sid(id)
);