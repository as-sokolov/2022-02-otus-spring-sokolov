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
import ru.otus.spring.models.Author;
import ru.otus.spring.repositories.AuthorRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@DisplayName("Сервис для работы с авторами должен ")
@SpringBootTest
@DirtiesContext
public class AuthorServiceTest {

    @Autowired
    private AuthorService authorService;

    @MockBean
    private AuthorRepository authorRepository;

    @Test
    @DisplayName("возвращать ошибку для анонимного пользователя")
    @WithAnonymousUser
    void shouldThrowExceptionGetAllAuthors() {
        Mockito.when(authorRepository.findAll()).thenReturn(Collections.singletonList(new Author(1, "Author")));
        assertThrows(AccessDeniedException.class, authorService::getAllAuthors);
    }

    @Test
    @DisplayName("возвращать ошибку для пользователя с неправильной ролью")
    @WithMockUser(roles = "SOME_ROLE")
    void shouldThrowExceptionBadRoleAllAuthors() {
        Mockito.when(authorRepository.findAll()).thenReturn(Collections.singletonList(new Author(1, "Author")));
        assertThrows(AccessDeniedException.class, authorService::getAllAuthors);
    }

    @Test
    @DisplayName("возвращать корректный результат для пользователя с правильной ролью")
    @WithMockUser(roles = "PUSHKIN")
    void shouldReturnAllAuthors() {
        List<Author> authorList = Collections.singletonList(new Author(1, "Author"));
        Mockito.when(authorRepository.findAll()).thenReturn(authorList);
        assertEquals(authorList, authorService.getAllAuthors());
    }

    @Test
    @DisplayName("возвращать ошибку для анонимного пользователя")
    @WithAnonymousUser
    void shouldThrowExceptionGetAuthor() {
        Mockito.when(authorRepository.getById(any())).thenReturn(new Author(1, "Author"));
        assertThrows(AccessDeniedException.class, () -> {
            authorService.getAuthor(1L);
        });
    }

    @Test
    @DisplayName("возвращать ошибку для пользователя с неправильной ролью")
    @WithMockUser(roles = "SOME_ROLE")
    void shouldThrowExceptionBadRoleGetAuthor() {
        Mockito.when(authorRepository.getById(any())).thenReturn(new Author(1, "Author"));
        assertThrows(AccessDeniedException.class, () -> {
            authorService.getAuthor(1L);
        });
    }

    @Test
    @DisplayName("возвращать корректный результат для пользователя с правильной ролью")
    @WithMockUser(roles = "PUSHKIN")
    void shouldReturnGetAuthor() {
        Author author = new Author(1, "Author");
        Mockito.when(authorRepository.getById(1L)).thenReturn(author);
        assertEquals(author, authorService.getAuthor(1L));
    }

    @Test
    @DisplayName("возвращать ошибку для анонимного пользователя при удалении автора")
    @WithAnonymousUser
    void shouldThrowExceptionDeleteAuthor() {
        assertThrows(AccessDeniedException.class, () -> {
            authorService.deleteAuthor(1L);
        });
    }

    @Test
    @DisplayName("возвращать ошибку для пользователя с неправильной ролью при удалении автора")
    @WithMockUser(roles = "PUSHKIN")
    void shouldThrowExceptionBadRoleDeleteAuthor() {
        assertThrows(AccessDeniedException.class, () -> {
            authorService.deleteAuthor(1L);
        });
    }

    @Test
    @DisplayName("возвращать корректный результат для пользователя с правильной ролью при удалении автора")
    @WithMockUser(roles = "ADMIN")
    void shouldExecuteDelete() {
        authorService.deleteAuthor(1L);
    }

    @Test
    @DisplayName("возвращать ошибку для анонимного пользователя при добавлении автора")
    @WithAnonymousUser
    void shouldThrowExceptionAddAuthor() {
        Author author = new Author(1, "author");
        Mockito.when(authorRepository.save(author)).thenReturn(author);
        assertThrows(AccessDeniedException.class, () -> {
            authorService.addAuthor(author);
        });
    }

    @Test
    @DisplayName("возвращать ошибку для пользователя с неправильной ролью при добавлении автора")
    @WithMockUser(roles = "PUSHKIN")
    void shouldThrowExceptionBadRoleAddAuthor() {
        Author author = new Author(1, "author");
        Mockito.when(authorRepository.save(author)).thenReturn(author);
        assertThrows(AccessDeniedException.class, () -> {
            authorService.addAuthor(author);
        });
    }

    @Test
    @DisplayName("возвращать корректный результат для пользователя с правильной ролью при добавлении автора")
    @WithMockUser(roles = "ADMIN")
    void shouldExecuteAdd() {
        Author author = new Author(1, "author");
        Mockito.when(authorRepository.save(author)).thenReturn(author);
        assertEquals(author, authorService.addAuthor(author));
    }

}
