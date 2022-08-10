package ru.otus.spring.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.models.Author;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Comment;
import ru.otus.spring.models.Genre;
import ru.otus.spring.repositories.AuthorRepository;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.repositories.GenreRepository;
import javax.annotation.security.RolesAllowed;
import java.util.Collections;
import java.util.List;


@DisplayName("Сервис для работы с книгами должен ")
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext
public class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Test
    @DisplayName("возвращать книги автора пользователя")
    @WithMockUser(roles = "PUSHKIN")
    @Order(1)
    void shouldReturn2Books() {
        assertEquals(2, bookService.getAllBooks().size());
    }

    @Test
    @DisplayName("удалять книгу администратором")
    @WithMockUser(roles = "ADMIN")
    @Order(2)
    void shouldThrowExceptionBadRoleGetBooks() {
        assertEquals(4, bookService.getAllBooks().size());
    }

    @Test
    @DisplayName("возвращать ошибку для неправильного пользователя")
    @WithMockUser(roles = "PUSHKIN")
    @Order(3)
    void shouldNoReturnBook() {
        assertThrows(AccessDeniedException.class, () -> {
            bookService.getBook(1L);
        });
    }

    @Test
    @DisplayName("удалять книгу администратором")
    @WithMockUser(roles = "ADMIN")
    @Order(4)
    void shouldThrowExceptionBadRoleGetBook() {
        assertEquals(1, bookService.getBook(1L).getId());
    }

    @Test
    @DisplayName("возвращать ошибку для неправильного пользователя")
    @WithMockUser(roles = "PUSHKIN")
    @Order(5)
    void shouldThrowExceptionDeleteBook() {
        assertThrows(AccessDeniedException.class, () -> {
            bookService.deleteBook(3L);
        });
    }

    @Test
    @DisplayName("удалять книгу администратором")
    @WithMockUser(roles = "ADMIN")
    @Order(6)
    void shouldThrowExceptionBadRoleDeleteBook() {
        bookService.deleteBook(3L);
    }

    @Test
    @DisplayName("возвращать ошибку для неправильного пользователя")
    @WithMockUser(roles = "PUSHKIN")
    @Order(7)
    void shouldThrowExceptionSaveBook() {
        assertThrows(AccessDeniedException.class, () -> {
            bookService.addBook(new Book());
        });
    }

    @Test
    @DisplayName("добавлять книгу администратором")
    @WithMockUser(roles = "ADMIN")
    @Order(8)
    void shouldSaveBook() {
        Book book = new Book(0, "test", List.of(new Author(1 , "Маша")), new Genre(1, "Детектив"), Collections.EMPTY_LIST);
        bookService.addBook(book);
    }
}
