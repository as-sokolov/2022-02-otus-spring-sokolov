package ru.otus.spring.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.models.Genre;
import ru.otus.spring.service.GenreService;
import java.util.List;

@WebMvcTest(GenreController.class)
public class GenreControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private GenreService genreService;

    @Test
    void shouldReturnCorrectGenreList() throws Exception {
        List<Genre> genres = List.of(new Genre(1, "Genre1"), new Genre(2, "Genre2"));
        given(genreService.getAllGenres()).willReturn(genres);

        mvc.perform(get("/api/genre"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(genres)));
    }


    @Test
    void shouldCorrectDeleteGenre() throws Exception {
        mvc.perform(delete("/api/genre/1"))
                .andExpect(status().isOk());
        verify(genreService, times(1)).deleteGenre(1L);
    }

    @Test
    void shouldCorrectSaveNewGenre() throws Exception {
        Genre genre = new Genre(1, "Genre1");
        given(genreService.addGenre(any())).willReturn(genre);
        String expectedResult = mapper.writeValueAsString(genre);

        mvc.perform(post("/api/genre").contentType(APPLICATION_JSON)
                .content(expectedResult))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void shouldThrowExceptionSaveNewGenre() throws Exception {
        Genre genre = new Genre(1, "Genre1");
        given(genreService.addGenre(any())).willThrow(new DataIntegrityViolationException("Exception"));
        String expectedResult = mapper.writeValueAsString(genre);

        mvc.perform(post("/api/genre").contentType(APPLICATION_JSON)
                .content(expectedResult))
                .andExpect(status().isOk())
                .andExpect(content().string("Жанр с наименованием Genre1 уже существует "));
    }

    @Test
    void shouldThrowExceptionDeleteGenre() throws Exception {
        doThrow(new DataIntegrityViolationException("Exception")).when(genreService).deleteGenre(any());


        mvc.perform(delete("/api/genre/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Нельзя удалить жанр c id 1, пока он используется для одной из книг"));
        verify(genreService, times(1)).deleteGenre(1L);
    }

}
