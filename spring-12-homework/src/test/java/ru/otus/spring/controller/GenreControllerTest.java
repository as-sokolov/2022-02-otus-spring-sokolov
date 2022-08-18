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
import ru.otus.spring.models.Genre;
import ru.otus.spring.security.SecurityConfiguration;
import ru.otus.spring.service.GenreService;
import ru.otus.spring.service.UserAuthorizationServiceImpl;
import java.util.Collections;

@WebMvcTest(GenreController.class)
@ContextConfiguration(classes = {GenreController.class, SecurityConfiguration.class})
public class GenreControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GenreService genreService;

    @MockBean
    private UserAuthorizationServiceImpl userAuthorizationService;


    @WithAnonymousUser
    @DisplayName("должен получать редирект")
    @Test
    void shouldGetRedirectWithAnonymousUser() throws Exception {
        Genre genre = new Genre(1, "name");
        when(genreService.getAllGenres()).thenReturn(Collections.singletonList(genre));

        mvc.perform(MockMvcRequestBuilders.get("/genres/"))
                .andExpect(status().is3xxRedirection());

    }

    @WithMockUser()
    @DisplayName("должен получать список жанров")
    @Test
    void shouldGetGenresList() throws Exception {
        Genre genre = new Genre(1, "name");
        when(genreService.getAllGenres()).thenReturn(Collections.singletonList(genre));

        mvc.perform(MockMvcRequestBuilders.get("/genres/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("name")));

    }

    @WithMockUser()
    @DisplayName("должен получать вью для добавления жанр")
    @Test
    void shouldGetGenre() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/genres/add"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Добавление жанра")));

    }

    @WithMockUser()
    @DisplayName("должен сохранять жанр")
    @Test
    void shouldSaveGenre() throws Exception {
        when(genreService.addGenre(any())).thenReturn(new Genre(1, "name"));
        mvc.perform(MockMvcRequestBuilders.post("/genres/add").param("name", "name"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/genres/"));
    }

    @WithMockUser()
    @DisplayName("должен удалять жанр")
    @Test
    void shouldDeleteGenre() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/genres/delete").param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/genres/"));
    }
}
