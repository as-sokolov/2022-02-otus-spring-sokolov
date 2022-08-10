insert into AUTHORS (id, name) (select nextval('authors_seq'), 'Маша' from dual);
insert into AUTHORS (id, name) (select nextval('authors_seq'), 'Иван' from dual);
insert into AUTHORS (id, name) (select nextval('authors_seq'), 'Петя' from dual);
insert into AUTHORS (id, name) (select nextval('authors_seq'), 'Пушкин' from dual);

insert into GENRES (id, name) (select nextval('genres_seq'), 'Детектив' from dual);
insert into GENRES (id, name) (select nextval('genres_seq'), 'Фантастика' from dual);
insert into BOOKS (id, name, GENRE_ID) (select nextval('books_seq'), 'Убийство в отблесках мониторов', gn.id from genres gn where gn.name = 'Детектив');
insert into BOOKS (id, name, GENRE_ID) (select nextval('books_seq'), 'Евгений Онегин', gn.id from genres gn where gn.name = 'Детектив');
insert into BOOKS (id, name, GENRE_ID) (select nextval('books_seq'), 'Фокусы и игры', gn.id from genres gn where gn.name = 'Фантастика');
insert into BOOKS (id, name, GENRE_ID) (select nextval('books_seq'), 'Сказка о золотой рыбке', gn.id from genres gn where gn.name = 'Фантастика');
insert into COMMENTS (id, text, book_id) (select nextval('comments_seq'), 'Книга норм', bk.id from books bk where bk.name = 'Убийство в отблесках мониторов');
insert into COMMENTS (id, text, book_id) (select nextval('comments_seq'), 'Так себе', bk.id from books bk where bk.name = 'Убийство в отблесках мониторов');
insert into BOOKS_AUTHORS (book_id, author_id) values (1,1);
insert into BOOKS_AUTHORS (book_id, author_id) values (2,4);
insert into BOOKS_AUTHORS (book_id, author_id) values (3,2);
insert into BOOKS_AUTHORS (book_id, author_id) values (4,4);

insert into USERS (id, login, hash, enabled, accountnonexpired, credentialsnonexpired, nonlocked, role)
(select nextval('users_seq'), 'admin', '$2a$10$y73iSSIkoxdOLBto692uPuYeIQRz4MTADOzzWVBVyXuKX2SpT4MUa', true, true, true, true, 'ROLE_ADMIN' from dual);
insert into USERS (id, login, hash, enabled, accountnonexpired, credentialsnonexpired, nonlocked, role)
(select nextval('users_seq'), 'user', '$2a$10$2XpZJYPbZHzLpftPymnMduKTJGwKiGgxgs2K9o2toRi/YrbNidT36', true, true, true, true, 'ROLE_USER' from dual);
insert into USERS (id, login, hash, enabled, accountnonexpired, credentialsnonexpired, nonlocked, role)
(select nextval('users_seq'), 'pushkin', '$2a$10$GC99CHlFeO/sqvlrOmOCpuhjlRAVV9NkIjGXF9s4u4S9D0JxKdelC', true, true, true, true, 'ROLE_PUSHKIN' from dual);


INSERT INTO acl_sid (id, principal, sid) VALUES
(1, 0, 'ROLE_ADMIN'),
(2, 0, 'ROLE_USER'),
(3, 0, 'ROLE_PUSHKIN');

INSERT INTO acl_class (id, class) VALUES
(1, 'ru.otus.spring.models.Book');

INSERT INTO acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES
(1, 1, NULL, 1, 0),
(1, 2, NULL, 3, 0),
(1, 3, NULL, 1, 0),
(1, 4, NULL, 3, 0);

INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask,
                       granting, audit_success, audit_failure) VALUES
(1, 1, 1, 1, 1, 1, 1),
(1, 2, 2, 1, 1, 1, 1),
(1, 4, 1, 2, 1, 1, 1),

(2, 1, 1, 1, 1, 1, 1),
(2, 2, 2, 1, 1, 1, 1),
(2, 3, 3, 1, 1, 1, 1),
(2, 4, 1, 2, 1, 1, 1),
(2, 5, 3, 2, 1, 1, 1),

(3, 1, 1, 1, 1, 1, 1),
(3, 2, 2, 1, 1, 1, 1),
(3, 4, 1, 2, 1, 1, 1),

(4, 1, 1, 1, 1, 1, 1),
(4, 2, 2, 1, 1, 1, 1),
(4, 3, 3, 1, 1, 1, 1),
(4, 4, 1, 2, 1, 1, 1),
(4, 5, 3, 2, 1, 1, 1);






