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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.spring.models.Author;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Genre;
import ru.otus.spring.security.SecurityConfiguration;
import ru.otus.spring.service.AuthorService;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.GenreService;
import ru.otus.spring.service.UserAuthorizationServiceImpl;
import java.util.Collections;

@WebMvcTest(BookController.class)
@ContextConfiguration(classes = {BookController.class, SecurityConfiguration.class})
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private AuthorService authorService;


    @MockBean
    private UserAuthorizationServiceImpl userAuthorizationService;


    @WithAnonymousUser
    @DisplayName("должен получать редирект")
    @Test
    void shouldGetRedirectWithAnonymousUser() throws Exception {
        Book book = new Book();
        book.setName("Название книги");
        when(bookService.getAllBooks()).thenReturn(Collections.singletonList(book));

        mvc.perform(MockMvcRequestBuilders.get("/books/"))
                .andExpect(status().is3xxRedirection());

    }

    @WithMockUser()
    @DisplayName("должен получать список книг")
    @Test
    void shouldGetBooksList() throws Exception {
        Book book = new Book();
        book.setName("Название книги");
        book.setGenre(new Genre(1,"Genre"));
        book.setAuthorList(Collections.singletonList(new Author(3, "author")));
        when(bookService.getAllBooks()).thenReturn(Collections.singletonList(book));

        mvc.perform(MockMvcRequestBuilders.get("/books/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Название книги")));
    }

    @WithMockUser()
    @DisplayName("должен получать вью для добавления книги")
    @Test
    void shouldGetBook() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/books/add/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Добавление книги")));

    }

    @WithMockUser()
    @DisplayName("должен сохранять книгу")
    @Test
    void shouldSaveBook() throws Exception {
        when(bookService.addBook(any(Book.class))).thenReturn(new Book());
        mvc.perform(MockMvcRequestBuilders.post("/books/add/")
                .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/"));
    }

    @WithMockUser()
    @DisplayName("должен удалять книгу")
    @Test
    void shouldDeleteBook() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/books/delete/?id=1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/"));
    }



    @WithMockUser()
    @DisplayName("должен получать вью для изменения книги")
    @Test
    void shouldGetUpdateBook() throws Exception {
        Book book = new Book();
        book.setName("Название книги");
        book.setGenre(new Genre(1,"Genre"));
        book.setAuthorList(Collections.singletonList(new Author(3, "author")));

        when(bookService.getBook(any())).thenReturn(book);
        mvc.perform(MockMvcRequestBuilders.get("/books/edit/?id=1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Изменение книги")));
    }

    @WithMockUser()
    @DisplayName("должен изменять книгу")
    @Test
    void shouldUpdateBook() throws Exception {
        when(bookService.addBook(any(Book.class))).thenReturn(new Book());
        mvc.perform(MockMvcRequestBuilders.post("/books/edit/")
                .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/"));
    }
}
