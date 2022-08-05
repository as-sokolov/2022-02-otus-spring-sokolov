package ru.otus.spring.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Comment;
import ru.otus.spring.models.Genre;
import ru.otus.spring.security.SecurityConfiguration;
import ru.otus.spring.service.CommentService;
import ru.otus.spring.service.UserAuthorizationServiceImpl;
import java.util.Collections;

@WebMvcTest(CommentController.class)
@ContextConfiguration(classes = {CommentController.class, SecurityConfiguration.class})
public class CommentControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CommentService commentService;

    @MockBean
    private UserAuthorizationServiceImpl userAuthorizationService;


    @WithAnonymousUser
    @DisplayName("должен получать редирект")
    @Test
    void shouldGetRedirectWithAnonymousUser() throws Exception {
        Comment comment = new Comment(1, "name", null);
        when(commentService.getComments(any())).thenReturn(Collections.singletonList(comment));

        mvc.perform(MockMvcRequestBuilders.get("/comments/?id=1"))
                .andExpect(status().is3xxRedirection());

    }

    @WithMockUser()
    @DisplayName("должен получать список комментариев")
    @Test
    void shouldGetCommentsList() throws Exception {
        Comment comment = new Comment(1, "Комментарий", null);
        when(commentService.getComments(any())).thenReturn(Collections.singletonList(comment));

        mvc.perform(MockMvcRequestBuilders.get("/comments/?id=1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Комментарий")));

    }

    @WithMockUser()
    @DisplayName("должен получать вью для добавления комментария")
    @Test
    void shouldGetComment() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/comments/add/?bookId=3"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Добавление комментария")));

    }

    @WithMockUser()
    @DisplayName("должен сохранять комментарий")
    @Test
    void shouldSaveComment() throws Exception {
        when(commentService.saveComment(any(Long.class), any(Comment.class))).thenReturn(new Comment(1, "name", null));
        mvc.perform(MockMvcRequestBuilders.post("/comments/add/")
                .param("text", "1234")
                .param("book.id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/comments/?id=1"));
    }

    @WithMockUser()
    @DisplayName("должен удалять комментарий")
    @Test
    void shouldDeleteComment() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/comments/delete/?id=1&bookId=3").param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/comments/?id=3"));
    }
}
