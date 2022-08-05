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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.dto.AuthorDto;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.models.Author;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Comment;
import ru.otus.spring.models.Genre;
import ru.otus.spring.service.BookService;
import java.util.ArrayList;
import java.util.List;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookService bookService;

    @Test
    void shouldReturnCorrectBookList() throws Exception {
        List<Book> books = List.of(new Book(1, "Book1", new ArrayList<Author>(), new Genre(1, "Genre1"), new ArrayList<Comment>()), new Book(2, "Book2",
                new ArrayList<Author>(), new Genre(1, "Genre2"), new ArrayList<Comment>()));
        given(bookService.getAllBooks()).willReturn(books);

        mvc.perform(get("/api/book"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(books)));
    }

    @Test
    void shouldReturnCorrectBookById() throws Exception {
        Book book = new Book(1, "Book1", new ArrayList<Author>(), new Genre(1, "Genre1"), new ArrayList<Comment>());
        given(bookService.getBook(1L)).willReturn(book);

        mvc.perform(get("/api/book/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(book)));
    }

    @Test
    void shouldCorrectDeleteBook() throws Exception {
        mvc.perform(delete("/api/book/1"))
                .andExpect(status().isOk());
        verify(bookService, times(1)).deleteBook(1L);
    }

    @Test
    void shouldCorrectSaveNewBook() throws Exception {
        Book book = new Book(1, "Book1", new ArrayList<Author>(), new Genre(1, "Genre1"), new ArrayList<Comment>());
        given(bookService.addBook(any())).willReturn(book);
        String expectedResult = mapper.writeValueAsString(book);

        mvc.perform(post("/api/book").contentType(APPLICATION_JSON)
                .content(expectedResult))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void shouldCorrectUpdateNewBook() throws Exception {
        Book book = new Book(1, "Book1", new ArrayList<Author>(), new Genre(1, "Genre1"), new ArrayList<Comment>());
        given(bookService.updateBook(book)).willReturn(book);
        String expectedResult = mapper.writeValueAsString(BookDto.toDto(book));

        mvc.perform(put("/api/book/1").contentType(APPLICATION_JSON)
                .content(expectedResult))
                .andExpect(status().isOk());
        verify(bookService, times(1)).updateBook(any());
    }

    @Test
    void shouldThrowExceptionDeleteBook() throws Exception {
        doThrow(new DataIntegrityViolationException("Exception")).when(bookService).deleteBook(any());


        mvc.perform(delete("/api/book/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("При удалении книги c id = 1 произошла ошибка"));
        verify(bookService, times(1)).deleteBook(1L);
    }

}
