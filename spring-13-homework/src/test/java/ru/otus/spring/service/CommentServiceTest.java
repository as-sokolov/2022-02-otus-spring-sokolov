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
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.models.Author;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Comment;
import ru.otus.spring.repositories.AuthorRepository;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.repositories.CommentRepository;
import javax.annotation.security.RolesAllowed;
import java.util.Collections;
import java.util.List;

@DisplayName("Сервис для работы с комментариями должен ")
@SpringBootTest
@DirtiesContext
public class CommentServiceTest {
    @Autowired
    private CommentService commentService;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private BookRepository bookRepository;

    @Test
    @DisplayName("возвращать ошибку для пользователя с неправильной ролью")
    @WithMockUser(roles = "SOME_ROLE")
    void shouldThrowExceptionBadRoleGetComment() {
        List<Comment> comments = Collections.singletonList(new Comment(1, "Comment", null));
        Mockito.when(commentRepository.getAllByBookId(any())).thenReturn(comments);
        assertThrows(AccessDeniedException.class, () -> {
            commentService.getComments(1L);
        });
    }

    @Test
    @DisplayName("возвращать корректный результат для пользователя с правильной ролью")
    @WithMockUser(roles = "PUSHKIN")
    void shouldReturnGetComment() {
        List<Comment> comments = Collections.singletonList(new Comment(1, "Comment", null));
        Mockito.when(commentRepository.getAllByBookId(any())).thenReturn(comments);
        assertEquals(comments, commentService.getComments(1L));
    }

    @Test
    @DisplayName("возвращать ошибку для пользователя с неправильной ролью")
    @WithMockUser(roles = "PUSHKIN")
    void shouldThrowExceptionBadRoleDeleteComment() {
        assertThrows(AccessDeniedException.class, () -> {
            commentService.deleteCommentById(1);
        });
    }

    @Test
    @DisplayName("возвращать корректный результат для пользователя с правильной ролью")
    @WithMockUser(roles = "ADMIN")
    void shouldExecuteDeleteComment() {
        commentService.deleteCommentById(1);
    }

    @Test
    @DisplayName("возвращать ошибку для пользователя с неправильной ролью")
    @WithMockUser(roles = "PUSHKIN")
    void shouldThrowExceptionBadRoleSaveComment() {
        assertThrows(AccessDeniedException.class, () -> {
            commentService.saveComment(1, new Comment(1, "comment", null));
        });
    }

    @Test
    @DisplayName("возвращать корректный результат для пользователя с правильной ролью")
    @WithMockUser(roles = "ADMIN")
    void shouldExecuteSaveComment() {
        Comment comment = new Comment(1, "comment", null);
        Book book = new Book();
        Mockito.when(commentRepository.save(any())).thenReturn(comment);
        Mockito.when(bookRepository.getById(any())).thenReturn(book);
        commentService.saveComment(1, comment);
    }
}
