insert into AUTHORS (`name`) values ('Маша');
insert into GENRES (name) values ('Детектив');
insert into BOOKS (name, GENRE_ID)
(
  select 'Убийство в отблесках мониторов', gn.id from genres gn where gn.name = 'Детектив'
);
insert into BOOKS_AUTHORS (book_id, author_id)
(
  select bk.id book_id, au.id author_id from authors au, BOOKS bk
   where au.name = 'Маша' and bk.name = 'Убийство в отблесках мониторов'
);


