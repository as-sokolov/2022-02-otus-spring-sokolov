package ru.otus.spring.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.TestPropertySource;
import ru.otus.spring.models.Author;
import java.util.List;

@DisplayName("Dao для работы с авторами должно ")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataMongoTest
@TestPropertySource(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration")
class AuthorRepositoryTest {

    private static final int EXPECTED_AUTHORS_COUNT = 2;
    private static final String EXISTING_AUTHORS_ID = "1";
    private static final String EXISTING_AUTHOR_NAME = "Маша";
    private static final String INSERT_AUTHOR_NAME = "Иван2";

    @Autowired
    private AuthorRepository authorRepository;


    @DisplayName("возвращать ожидаемый список авторов в БД")
    @Test
    @Order(3)
    void shouldReturnExpectedAuthors() {
        Author expectedAuthor = new Author("1", EXISTING_AUTHOR_NAME);
        List<Author> authors = authorRepository.findAll();
        assertThat(authors.size()).isEqualTo(EXPECTED_AUTHORS_COUNT);
        assertThat(authors.get(0).getName()).isEqualTo(expectedAuthor.getName());
    }

    @DisplayName("добавлять и удалять автора в БД")
    @Test
    @Order(2)
    void shouldInsertAuthor() {
        Author expectedAuthor = new Author(null, INSERT_AUTHOR_NAME);
        Author author = authorRepository.save(expectedAuthor);
        Author actualAuthor = authorRepository.findById(author.getId()).orElse(null);
        assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
        authorRepository.deleteById(actualAuthor.getId());
        assertThat(authorRepository.existsById(actualAuthor.getId())).isFalse();
    }

    @DisplayName("возвращать ожидаемого автора по его id")
    @Test
    @Order(1)
    void shouldReturnExpectedAuthorById() {
        Author expectedAuthor = new Author(EXISTING_AUTHORS_ID, EXISTING_AUTHOR_NAME);
        Author actualAuthor = authorRepository.findById(expectedAuthor.getId()).orElse(null);
        assertThat(actualAuthor.getName()).isEqualTo(expectedAuthor.getName());
    }
}