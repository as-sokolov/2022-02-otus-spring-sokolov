package ru.otus.spring.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class BookDaoJdbc implements BookDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public BookDaoJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public Book getById(long id) {
        try {
            BookRowCallbackHandler bookRowCallbackHandler = new BookRowCallbackHandler();
            Map<String, Object> params = Collections.singletonMap("id", id);
            namedParameterJdbcOperations.query(
                    "select bk.id, bk.name, gn.id genre_id, gn.name genre_name, au.id author_id, au.name author_name from books bk join genres gn " +
                            "on bk.genre_id = gn.id join books_authors ba on bk.id = ba.book_id join authors au on ba.author_id = au.id where bk.id = :id",
                    params, bookRowCallbackHandler);
            List<Book> books = bookRowCallbackHandler.getBooksList();

            // вместо этого if можно было бы добавить в catch ArrayIndexOutOfBoundsException, но исключения обрабатываются сильно дольше
            // обычного кода, поэтому пусть здесь будет проверка
            if (books.size() == 0) {
                log.debug("Запись с id {} не найдена", id);
                return null;
            }

            return books.get(0);
        } catch (DataAccessException ex) {
            log.debug("Запись с id {} не найдена", id);
            return null;
        }
    }

    @Override
    public List<Book> getAll() {
        List<Book> bookList;
        try {
            BookRowCallbackHandler bookRowCallbackHandler = new BookRowCallbackHandler();
            namedParameterJdbcOperations.query("select bk.id, bk.name, gn.id genre_id, gn.name genre_name, au.id author_id, au.name author_name from books bk join genres gn " +
                    "on bk.genre_id = gn.id join books_authors ba on bk.id = ba.book_id join authors au on ba.author_id = au.id", bookRowCallbackHandler);
            bookList = bookRowCallbackHandler.getBooksList();
        } catch (EmptyResultDataAccessException ex) {
            log.debug("Записей не найдено");
            return new ArrayList<>();
        }

        return bookList;
    }

    @Override
    public Long insert(Book book) {

        KeyHolder kh = new GeneratedKeyHolder();
        namedParameterJdbcOperations.update("insert into books (name, genre_id) values (:name, :genre_id)",
                new MapSqlParameterSource().addValue("name", book.getName())
                        .addValue("genre_id", book.getGenre() == null ? null : book.getGenre().getId()), kh);
        if (kh.getKey() != null && book.getAuthorList() != null) {
            for (Long id : book.getAuthorList().stream().map(Author::getId).distinct().collect(Collectors.toList())) {
                if (id != null && id != 0) {
                    namedParameterJdbcOperations.update("insert into books_authors (book_id, author_id) values (:book_id, :author_id)",
                            new MapSqlParameterSource().addValue("book_id", kh.getKeyAs(Long.class)).addValue("author_id", id));
                }
            }
        }
        return kh.getKey() == null ? null : kh.getKeyAs(Long.class);
    }

    @Override
    public void update(Book book) {

        namedParameterJdbcOperations.update("update books set name = :name, genre_id = :genre_id where id = :id",
                new MapSqlParameterSource().addValue("name", book.getName())
                        .addValue("genre_id", book.getGenre() == null ? null : book.getGenre().getId())
                        .addValue("id", book.getId()));
        namedParameterJdbcOperations.update("delete from books_authors where book_id = :id", new MapSqlParameterSource().addValue("id", book.getId()));

        if (book.getAuthorList() != null) {
            // с помощью стрима оставляем только уникальных авторов (если вдруг были указаны одинаковые id
            for (Long id : book.getAuthorList().stream().map(Author::getId).distinct().collect(Collectors.toList())) {
                if (id != null && id != 0) {
                    namedParameterJdbcOperations.update("insert into books_authors (book_id, author_id) values (:book_id, :author_id)",
                            new MapSqlParameterSource().addValue("book_id", book.getId()).addValue("author_id", id));
                }
            }
        }
    }

    @Override
    public void deleteById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        namedParameterJdbcOperations.update(
                "delete from books_authors where book_id = :id", params
        );
        namedParameterJdbcOperations.update(
                "delete from books where id = :id", params
        );
    }


    private static class BookRowCallbackHandler implements RowCallbackHandler {
        private Map<Long, Book> books = new HashMap<>();

        public List<Book> getBooksList() {
            return List.of(books.values().toArray(new Book[0]));
        }

        @Override
        public void processRow(ResultSet resultSet) throws SQLException {
            // если книги еще нет, добавляем ее в Map
            if (!books.containsKey(resultSet.getLong("id"))) {
                books.put(resultSet.getLong("id"), new Book(
                        resultSet.getLong("id"), resultSet.getString("name"), new ArrayList<>(),
                        new Genre(resultSet.getLong("genre_id"), resultSet.getString("genre_name"))));
            }

            // берем из книг нужную, добавляем одного автора
            books.get(resultSet.getLong("id")).getAuthorList().add(new Author(resultSet.getLong("author_id"), resultSet.getString("author_name")));
        }
    }

}

