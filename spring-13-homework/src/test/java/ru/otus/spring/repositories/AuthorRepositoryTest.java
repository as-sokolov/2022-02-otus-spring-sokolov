package ru.otus.spring.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import ru.otus.spring.models.Author;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@DisplayName("Dao для работы с авторами должно ")
@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class AuthorRepositoryTest {

    private static final int EXPECTED_AUTHORS_COUNT = 4;
    private static final Long EXISTING_AUTHORS_ID = 1L;
    private static final String EXISTING_AUTHOR_NAME = "Маша";
    private static final String INSERT_AUTHOR_NAME = "Иван2";

    @Autowired
    private AuthorRepository authorRepository;

    @DisplayName("возвращать ожидаемый список авторов в БД")
    @Test
    @Order(3)
    void shouldReturnExpectedAuthors() {
        Author expectedAuthor = new Author(1, EXISTING_AUTHOR_NAME);
        List<Author> authors = authorRepository.findAll();
        assertThat(authors.size()).isEqualTo(EXPECTED_AUTHORS_COUNT);
        assertThat(authors.get(0).getName()).isEqualTo(expectedAuthor.getName());
    }

    @DisplayName("добавлять и удалять автора в БД")
    @Test
    @Order(2)
    void shouldInsertAuthor() {
        Author expectedAuthor = new Author(0, INSERT_AUTHOR_NAME);
        Author author = authorRepository.save(expectedAuthor);
        Author actualAuthor = authorRepository.getById(author.getId());
        assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
        authorRepository.deleteById(actualAuthor.getId());
        assertThat(authorRepository.existsById(actualAuthor.getId())).isFalse();
    }

    @DisplayName("возвращать ожидаемого автора по его id")
    @Test
    @Order(1)
    void shouldReturnExpectedAuthorById() {
        Author expectedAuthor = new Author(EXISTING_AUTHORS_ID, EXISTING_AUTHOR_NAME);
        Author actualAuthor = authorRepository.getById(expectedAuthor.getId());
        assertThat(actualAuthor.getName()).isEqualTo(expectedAuthor.getName());
    }
}