package ru.otus.spring.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.models.Genre;
import ru.otus.spring.repositories.GenreRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@DisplayName("Сервис для работы с жанрами должен ")
@SpringBootTest
@DirtiesContext
public class GenreServiceTest {

    @Autowired
    private GenreService genreService;

    @MockBean
    private GenreRepository genreRepository;

    @Test
    @DisplayName("возвращать ошибку для анонимного пользователя")
    @WithAnonymousUser
    void shouldThrowExceptionGetAllGenres() {
        Mockito.when(genreRepository.findAll()).thenReturn(Collections.singletonList(new Genre(1, "Genre")));
        assertThrows(AccessDeniedException.class, genreService::getAllGenres);
    }

    @Test
    @DisplayName("возвращать ошибку для пользователя с неправильной ролью")
    @WithMockUser(roles = "SOME_ROLE")
    void shouldThrowExceptionBadRoleAllGenres() {
        Mockito.when(genreRepository.findAll()).thenReturn(Collections.singletonList(new Genre(1, "Genre")));
        assertThrows(AccessDeniedException.class, genreService::getAllGenres);
    }

    @Test
    @DisplayName("возвращать корректный результат для пользователя с правильной ролью")
    @WithMockUser(roles = "PUSHKIN")
    void shouldReturnAllGenres() {
        List<Genre> genreList = Collections.singletonList(new Genre(1, "Genre"));
        Mockito.when(genreRepository.findAll()).thenReturn(genreList);
        assertEquals(genreList, genreService.getAllGenres());
    }

    @Test
    @DisplayName("возвращать ошибку для анонимного пользователя")
    @WithAnonymousUser
    void shouldThrowExceptionGetGenre() {
        Mockito.when(genreRepository.getById(any())).thenReturn(new Genre(1, "Genre"));
        assertThrows(AccessDeniedException.class, () -> {
            genreService.getGenre(1L);
        });
    }

    @Test
    @DisplayName("возвращать ошибку для пользователя с неправильной ролью")
    @WithMockUser(roles = "SOME_ROLE")
    void shouldThrowExceptionBadRoleGetGenre() {
        Mockito.when(genreRepository.getById(any())).thenReturn(new Genre(1, "Genre"));
        assertThrows(AccessDeniedException.class, () -> {
            genreService.getGenre(1L);
        });
    }

    @Test
    @DisplayName("возвращать корректный результат для пользователя с правильной ролью")
    @WithMockUser(roles = "PUSHKIN")
    void shouldReturnGetGenre() {
        Genre genre = new Genre(1, "Genre");
        Mockito.when(genreRepository.getById(1L)).thenReturn(genre);
        assertEquals(genre, genreService.getGenre(1L));
    }

    @Test
    @DisplayName("возвращать ошибку для анонимного пользователя при удалении жанра")
    @WithAnonymousUser
    void shouldThrowExceptionDeleteGenre() {
        assertThrows(AccessDeniedException.class, () -> {
            genreService.deleteGenre(1L);
        });
    }

    @Test
    @DisplayName("возвращать ошибку для пользователя с неправильной ролью при удалении жанра")
    @WithMockUser(roles = "PUSHKIN")
    void shouldThrowExceptionBadRoleDeleteGenre() {
        assertThrows(AccessDeniedException.class, () -> {
            genreService.deleteGenre(1L);
        });
    }

    @Test
    @DisplayName("возвращать корректный результат для пользователя с правильной ролью при удалении жанра")
    @WithMockUser(roles = "ADMIN")
    void shouldExecuteDelete() {
        genreService.deleteGenre(1L);
    }

    @Test
    @DisplayName("возвращать ошибку для анонимного пользователя при добавлении жанра")
    @WithAnonymousUser
    void shouldThrowExceptionAddGenre() {
        Genre genre = new Genre(1, "genre");
        Mockito.when(genreRepository.save(genre)).thenReturn(genre);
        assertThrows(AccessDeniedException.class, () -> {
            genreService.addGenre(genre);
        });
    }

    @Test
    @DisplayName("возвращать ошибку для пользователя с неправильной ролью при добавлении жанра")
    @WithMockUser(roles = "PUSHKIN")
    void shouldThrowExceptionBadRoleAddGenre() {
        Genre genre = new Genre(1, "genre");
        Mockito.when(genreRepository.save(genre)).thenReturn(genre);
        assertThrows(AccessDeniedException.class, () -> {
            genreService.addGenre(genre);
        });
    }

    @Test
    @DisplayName("возвращать корректный результат для пользователя с правильной ролью при добавлении жанра")
    @WithMockUser(roles = "ADMIN")
    void shouldExecuteAdd() {
        Genre genre = new Genre(1, "genre");
        Mockito.when(genreRepository.save(genre)).thenReturn(genre);
        assertEquals(genre, genreService.addGenre(genre));
    }
}
