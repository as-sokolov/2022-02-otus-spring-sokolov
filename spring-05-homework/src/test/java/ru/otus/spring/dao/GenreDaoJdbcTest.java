package ru.otus.spring.dao;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.Genre;
import java.util.List;

@DisplayName("Dao для работы с жанрами должно ")
@JdbcTest
@Import(GenreDaoJdbc.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GenreDaoJdbcTest {

    private static final int EXPECTED_GENRES_COUNT = 1;
    private static final int EXISTING_GENRES_ID = 1;
    private static final String EXISTING_GENRE_NAME = "Детектив";
    private static final String INSERT_GENRE_NAME = "Фантастика";


    @Autowired
    private GenreDaoJdbc genreDao;

    @DisplayName("возвращать ожидаемый список жанров в БД")
    @Test
    @Order(1)
    void shouldReturnExpectedGenres() {
        Genre expectedGenre = new Genre(1, EXISTING_GENRE_NAME);
        List<Genre> genres = genreDao.getAll();
        assertThat(genres.size()).isEqualTo(EXPECTED_GENRES_COUNT);
        assertThat(genres.get(0)).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("добавлять жанр в БД")
    @Test
    @Order(2)
    void shouldInsertGenre() {
        Genre expectedGenre = new Genre(INSERT_GENRE_NAME);
        Long id = genreDao.insert(expectedGenre);
        expectedGenre.setId(id);
        Genre actualGenre = genreDao.getById(id);
        assertThat(actualGenre).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("возвращать ожидаемый жанр по его id")
    @Test
    @Order(3)
    void shouldReturnExpectedGenreById() {
        Genre expectedGenre = new Genre(EXISTING_GENRES_ID, EXISTING_GENRE_NAME);
        Genre actualGenre = genreDao.getById(expectedGenre.getId());
        assertThat(actualGenre).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("удалять заданный жанр по его id")
    @Test
    @Order(4)
    void shouldCorrectDeleteGenreById() {
        Genre expectedGenre = new Genre(EXISTING_GENRES_ID, EXISTING_GENRE_NAME);
        assertThat(genreDao.getById(EXISTING_GENRES_ID)).isEqualTo(expectedGenre);
        genreDao.deleteById(EXISTING_GENRES_ID);
        assertThat(genreDao.getById(EXISTING_GENRES_ID)).isNull();
    }
}