package ru.otus.spring.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ru.otus.spring.models.Author;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@DisplayName("Dao для работы с авторами должно ")
@DataJpaTest
@Import({AuthorRepositoryJpa.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthorRepositoryJpaTest {

    private static final int EXPECTED_AUTHORS_COUNT = 3;
    private static final int EXISTING_AUTHORS_ID = 1;
    private static final String EXISTING_AUTHOR_NAME = "Маша";
    private static final String INSERT_AUTHOR_NAME = "Иван2";

    @Autowired
    private AuthorRepositoryJpa authorRepositoryJpa;

    @Autowired
    private EntityManager em;


    @DisplayName("возвращать ожидаемый список авторов в БД")
    @Test
    @Order(2)
    void shouldReturnExpectedAuthors() {
        Author expectedAuthor = new Author(1, EXISTING_AUTHOR_NAME);
        List<Author> authors = authorRepositoryJpa.getAll();
        assertThat(authors.size()).isEqualTo(EXPECTED_AUTHORS_COUNT);
        assertThat(authors.get(0)).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("добавлять автора в БД")
    @Test
    @Order(2)
    void shouldInsertAuthor() {
        Author expectedAuthor = new Author(0, INSERT_AUTHOR_NAME);
        Author author = authorRepositoryJpa.save(expectedAuthor);
        Author actualAuthor = authorRepositoryJpa.getById(author.getId());
        assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("возвращать ожидаемого автора по его id")
    @Test
    @Order(1)
    void shouldReturnExpectedAuthorById() {
        Author expectedAuthor = new Author(EXISTING_AUTHORS_ID, EXISTING_AUTHOR_NAME);
        Author actualAuthor = authorRepositoryJpa.getById(expectedAuthor.getId());
        assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("удалять заданного автора по его id")
    @Test
    @Order(4)
    void shouldCorrectDeleteAuthorById() {
        Author expectedAuthor = new Author(EXISTING_AUTHORS_ID, EXISTING_AUTHOR_NAME);
        assertThat(authorRepositoryJpa.getById(EXISTING_AUTHORS_ID)).isEqualTo(expectedAuthor);
        authorRepositoryJpa.deleteById(EXISTING_AUTHORS_ID);
        assertThat(authorRepositoryJpa.getById(EXISTING_AUTHORS_ID)).isNull();
    }
}