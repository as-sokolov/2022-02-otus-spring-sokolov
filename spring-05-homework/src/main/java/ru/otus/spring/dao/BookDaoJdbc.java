package ru.otus.spring.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class BookDaoJdbc implements BookDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    private final AuthorDao authorDao;

    public BookDaoJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations, AuthorDao authorDao) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
        this.authorDao = authorDao;
    }

    @Override
    public Book getById(long id) {
        Book book;
        try {
            Map<String, Object> params = Collections.singletonMap("id", id);
            book = namedParameterJdbcOperations.queryForObject(
                    "select bk.id, bk.name, gn.id genre_id, gn.name genre_name from books bk left join genres gn on bk.genre_id = gn.id where bk.id = :id", params,
                    new BookMapper()
            );
        } catch (EmptyResultDataAccessException ex) {
            log.debug("Запись с id {} не найдена", id);
            return null;
        }
        addBookAuthors(book);
        return book;
    }

    @Override
    public List<Book> getAll() {
        List<Book> bookList;
        try {
            bookList = namedParameterJdbcOperations.query("select bk.id, bk.name, gn.id genre_id, gn.name genre_name from books bk left join genres gn on" +
                    " bk.genre_id = gn.id", new BookMapper());
        } catch (EmptyResultDataAccessException ex) {
            log.debug("Записей не найдено");
            return new ArrayList<>();
        }

        for (Book book : bookList) {
            addBookAuthors(book);
        }
        return bookList;
    }

    @Override
    public Long insert(Book book) {
        if (!validateBook(book)) {
            log.error("Указаны некорректные значения параметров книги {}", book);
            return null;
        }

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
        if (!validateBook(book)) {
            log.error("Указаны некорректные значения параметров книги {}", book);
            return;
        }

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

    private static class BookMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet resultSet, int i) throws SQLException {
            return new Book(
                    resultSet.getLong("id"), resultSet.getString("name"), new ArrayList<>(),
                    new Genre(resultSet.getLong("genre_id"), resultSet.getString("genre_name")));
        }
    }

    private Book addBookAuthors(Book book) {
        List<Long> authorIdList = namedParameterJdbcOperations.queryForList(
                "select ba.author_id id from books bk " +
                        "join books_authors ba on bk.id = ba.book_id where bk.id = :id", Collections.singletonMap("id", book.getId()), Long.class);
        for (Long id : authorIdList) {
            book.getAuthorList().add(authorDao.getById(id));
        }
        return book;
    }

    private boolean validateBook(Book book) {
        // Проверяем, существует ли выбранный жанр
        Integer genreCount = book.getGenre() == null ? 0 :
                namedParameterJdbcOperations.queryForObject("select count (*) from genres gn where gn.id = :id",
                        new MapSqlParameterSource().addValue("id", book.getGenre().getId()), Integer.class);
        if (genreCount != 1) {
            return false;
        }

        // Проверяем, существует ли автор
        for (Author author : book.getAuthorList()) {
            if (author != null && author.getId() != 0) {
                // проверяем, существует ли автор с таким id
                Integer authorCount =
                        namedParameterJdbcOperations.queryForObject("select count (*) from authors au where au.id = :id",
                                new MapSqlParameterSource().addValue("id", author.getId()), Integer.class);
                if (authorCount != 1) {
                    return false;
                }
            }
        }
        return true;
    }
}

