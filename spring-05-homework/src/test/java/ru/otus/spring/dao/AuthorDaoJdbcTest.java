package ru.otus.spring.dao;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.Author;
import java.util.List;

@DisplayName("Dao для работы с авторами должно ")
@JdbcTest
@Import(AuthorDaoJdbc.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthorDaoJdbcTest {

    private static final int EXPECTED_AUTHORS_COUNT = 1;
    private static final int EXISTING_AUTHORS_ID = 1;
    private static final String EXISTING_AUTHOR_NAME = "Маша";
    private static final String INSERT_AUTHOR_NAME = "Иван";


    @Autowired
    private AuthorDaoJdbc authorDao;

    @DisplayName("возвращать ожидаемый список авторов в БД")
    @Test
    @Order(1)
    void shouldReturnExpectedAuthors() {
        Author expectedAuthor = new Author(1, EXISTING_AUTHOR_NAME);
        List<Author> authors = authorDao.getAll();
        assertThat(authors.size()).isEqualTo(EXPECTED_AUTHORS_COUNT);
        assertThat(authors.get(0)).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("добавлять автора в БД")
    @Test
    @Order(2)
    void shouldInsertAuthor() {
        Author expectedAuthor = new Author(INSERT_AUTHOR_NAME);
        Long id = authorDao.insert(expectedAuthor);
        expectedAuthor.setId(id);
        Author actualAuthor = authorDao.getById(id);
        assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("возвращать ожидаемого автора по его id")
    @Test
    @Order(3)
    void shouldReturnExpectedAuthorById() {
        Author expectedAuthor = new Author(EXISTING_AUTHORS_ID, EXISTING_AUTHOR_NAME);
        Author actualAuthor = authorDao.getById(expectedAuthor.getId());
        assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("удалять заданного автора по его id")
    @Test
    @Order(4)
    void shouldCorrectDeleteAuthorById() {
        Author expectedAuthor = new Author(EXISTING_AUTHORS_ID, EXISTING_AUTHOR_NAME);
        assertThat(authorDao.getById(EXISTING_AUTHORS_ID)).isEqualTo(expectedAuthor);
        authorDao.deleteById(EXISTING_AUTHORS_ID);
        assertThat(authorDao.getById(EXISTING_AUTHORS_ID)).isNull();
    }
}