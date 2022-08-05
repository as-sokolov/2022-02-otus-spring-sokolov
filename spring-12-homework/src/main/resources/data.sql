insert into AUTHORS (id, name) (select nextval('authors_seq'), 'Маша' from dual);
insert into AUTHORS (id, name) (select nextval('authors_seq'), 'Иван' from dual);
insert into AUTHORS (id, name) (select nextval('authors_seq'), 'Петя' from dual);
insert into GENRES (id, name) (select nextval('genres_seq'), 'Детектив' from dual);
insert into GENRES (id, name) (select nextval('genres_seq'), 'Фантастика' from dual);
insert into BOOKS (id, name, GENRE_ID) (select nextval('books_seq'), 'Убийство в отблесках мониторов', gn.id from genres gn where gn.name = 'Детектив');
insert into COMMENTS (id, text, book_id) (select nextval('comments_seq'), 'Книга норм', bk.id from books bk where bk.name = 'Убийство в отблесках мониторов');
insert into COMMENTS (id, text, book_id) (select nextval('comments_seq'), 'Так себе', bk.id from books bk where bk.name = 'Убийство в отблесках мониторов');
insert into BOOKS_AUTHORS (book_id, author_id) values (1,1);
insert into BOOKS_AUTHORS (book_id, author_id) values (1,2);
insert into USERS (id, login, hash, enabled, accountnonexpired, credentialsnonexpired, nonlocked, role)
(select nextval('users_seq'), 'admin', '$2a$10$skFrsCYWY83A8pxD27cFb.qO.6GlvWDhkoB1VLos.9M9hhThyA3j2', true, true, true, true, 'ROLE_ADMIN' from dual);
insert into USERS (id, login, hash, enabled, accountnonexpired, credentialsnonexpired, nonlocked, role)
(select nextval('users_seq'), 'user', '$2a$10$2XpZJYPbZHzLpftPymnMduKTJGwKiGgxgs2K9o2toRi/YrbNidT36', true, true, true, true, 'ROLE_USER' from dual);


