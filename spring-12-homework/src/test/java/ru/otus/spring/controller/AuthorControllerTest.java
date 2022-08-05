package ru.otus.spring.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.spring.models.Author;
import ru.otus.spring.security.SecurityConfiguration;
import ru.otus.spring.service.AuthorService;
import ru.otus.spring.service.UserAuthorizationServiceImpl;
import java.util.Collections;

@WebMvcTest(AuthorController.class)
@ContextConfiguration(classes = {AuthorController.class, SecurityConfiguration.class})
public class AuthorControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private UserAuthorizationServiceImpl userAuthorizationService;


    @WithAnonymousUser
    @DisplayName("должен получать редирект")
    @Test
    void shouldGetRedirectWithAnonymousUser() throws Exception {
        Author author = new Author(1, "name");
        when(authorService.getAllAuthors()).thenReturn(Collections.singletonList(author));

        mvc.perform(MockMvcRequestBuilders.get("/authors/"))
                .andExpect(status().is3xxRedirection());

    }

    @WithMockUser()
    @DisplayName("должен получать список авторов")
    @Test
    void shouldGetAuthorsList() throws Exception {
        Author author = new Author(1, "name");
        when(authorService.getAllAuthors()).thenReturn(Collections.singletonList(author));

        mvc.perform(MockMvcRequestBuilders.get("/authors/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("name")));

    }

    @WithMockUser()
    @DisplayName("должен получать вью для добавления автора")
    @Test
    void shouldGetAuthor() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/authors/add"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Добавление автора")));

    }

    @WithMockUser()
    @DisplayName("должен сохранять автора")
    @Test
    void shouldSaveAuthor() throws Exception {
        when(authorService.addAuthor(any())).thenReturn(new Author(1, "name"));
        mvc.perform(MockMvcRequestBuilders.post("/authors/add").param("name", "name"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/authors/"));
    }

    @WithMockUser()
    @DisplayName("должен удалять автора")
    @Test
    void shouldDeleteAuthor() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/authors/delete").param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/authors/"));
    }
}
