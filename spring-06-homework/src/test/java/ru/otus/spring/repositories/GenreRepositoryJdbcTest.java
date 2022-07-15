package ru.otus.spring.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.spring.models.Genre;
import java.util.List;

@DisplayName("Dao для работы с жанрами должно ")
@DataJpaTest
@Import(GenreRepositoryJpa.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GenreRepositoryJdbcTest {

    private static final int EXPECTED_GENRES_COUNT = 2;
    private static final int EXISTING_GENRES_ID = 1;
    private static final String EXISTING_GENRE_NAME = "Детектив";
    private static final String INSERT_GENRE_NAME = "Фантастика2";


    @Autowired
    private GenreRepositoryJpa genreRepositoryJpa;

    @DisplayName("возвращать ожидаемый список жанров в БД")
    @Test
    @Order(1)
    void shouldReturnExpectedGenres() {
        Genre expectedGenre = new Genre(1, EXISTING_GENRE_NAME);
        List<Genre> genres = genreRepositoryJpa.getAll();
        assertThat(genres.size()).isEqualTo(EXPECTED_GENRES_COUNT);
        assertThat(genres.get(0)).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("добавлять жанр в БД")
    @Test
    @Order(2)
    void shouldInsertGenre() {
        Genre expectedGenre = new Genre(0, INSERT_GENRE_NAME);
        Genre genre = genreRepositoryJpa.save(expectedGenre);
        assertThat(genre).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("возвращать ожидаемый жанр по его id")
    @Test
    @Order(3)
    void shouldReturnExpectedGenreById() {
        Genre expectedGenre = new Genre(EXISTING_GENRES_ID, EXISTING_GENRE_NAME);
        Genre actualGenre = genreRepositoryJpa.getById(expectedGenre.getId());
        assertThat(actualGenre).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("удалять заданный жанр по его id")
    @Test
    @Order(4)
    void shouldCorrectDeleteGenreById() {
        Genre expectedGenre = new Genre(EXISTING_GENRES_ID, EXISTING_GENRE_NAME);
        assertThat(genreRepositoryJpa.getById(EXISTING_GENRES_ID)).isEqualTo(expectedGenre);
        genreRepositoryJpa.deleteById(EXISTING_GENRES_ID);
        assertThat(genreRepositoryJpa.getById(EXISTING_GENRES_ID)).isNull();
    }
}