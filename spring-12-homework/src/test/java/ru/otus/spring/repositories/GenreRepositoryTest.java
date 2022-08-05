package ru.otus.spring.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.otus.spring.models.Genre;
import java.util.List;

@DisplayName("Dao для работы с жанрами должно ")
@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class GenreRepositoryTest {

    private static final int EXPECTED_GENRES_COUNT = 2;
    private static final Long EXISTING_GENRES_ID = 1L;
    private static final String EXISTING_GENRE_NAME = "Детектив";
    private static final String INSERT_GENRE_NAME = "Фантастика2";


    @Autowired
    private GenreRepository genreRepository;

    @DisplayName("возвращать ожидаемый список жанров в БД")
    @Test
    @Order(1)
    void shouldReturnExpectedGenres() {
        Genre expectedGenre = new Genre(1, EXISTING_GENRE_NAME);
        List<Genre> genres = genreRepository.findAll();
        assertThat(genres.size()).isEqualTo(EXPECTED_GENRES_COUNT);
        assertThat(genres.get(0).getName()).isEqualTo(expectedGenre.getName());
    }

    @DisplayName("добавлять и удалять жанр в БД")
    @Test
    @Order(2)
    void shouldInsertGenre() {
        Genre expectedGenre = new Genre(0, INSERT_GENRE_NAME);
        Genre genre = genreRepository.save(expectedGenre);
        assertThat(genre.getName()).isEqualTo(expectedGenre.getName());
        genreRepository.deleteById(genre.getId());
        assertThat(genreRepository.existsById(genre.getId())).isFalse();

    }

    @DisplayName("возвращать ожидаемый жанр по его id")
    @Test
    @Order(3)
    void shouldReturnExpectedGenreById() {
        Genre expectedGenre = new Genre(EXISTING_GENRES_ID, EXISTING_GENRE_NAME);
        Genre actualGenre = genreRepository.getById(expectedGenre.getId());
        assertThat(actualGenre.getName()).isEqualTo(expectedGenre.getName());
    }
}