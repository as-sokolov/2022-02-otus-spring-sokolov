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
import ru.otus.spring.models.Author;
import ru.otus.spring.service.AuthorService;
import java.util.List;

@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private AuthorService authorService;

    @Test
    void shouldReturnCorrectAuthorList() throws Exception {
        List<Author> authors = List.of(new Author(1, "Author1"), new Author(2, "Author2"));
        given(authorService.getAllAuthors()).willReturn(authors);

        mvc.perform(get("/api/author"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(authors)));
    }


    @Test
    void shouldCorrectDeleteAuthor() throws Exception {
        mvc.perform(delete("/api/author/1"))
                .andExpect(status().isOk());
        verify(authorService, times(1)).deleteAuthor(1L);
    }

    @Test
    void shouldCorrectSaveNewAuthor() throws Exception {
        Author author = new Author(1, "Author1");
        given(authorService.addAuthor(any())).willReturn(author);
        String expectedResult = mapper.writeValueAsString(author);

        mvc.perform(post("/api/author").contentType(APPLICATION_JSON)
                .content(expectedResult))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void shouldThrowExceptionDeleteAuthor() throws Exception {
        doThrow(new DataIntegrityViolationException("Exception")).when(authorService).deleteAuthor(any());


        mvc.perform(delete("/api/author/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Нельзя удалить автора c id 1, пока он используется для одной из книг"));
        verify(authorService, times(1)).deleteAuthor(1L);
    }


}
