package ru.otus.spring.controller;

import static org.mockito.ArgumentMatchers.any;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.models.Author;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Comment;
import ru.otus.spring.models.Genre;
import ru.otus.spring.repositories.BookRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@WebFluxTest
@ContextConfiguration(classes = CommentController.class)
public class CommentControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private BookRepository bookRepository;

    @Test
    public void shouldSaveComments() throws JsonProcessingException {
        Book book = getBook();
        Mono<Book> bookMono = Mono.just(book);
        Mockito.when(bookRepository.findById("3")).thenReturn(bookMono);
        Mockito.when(bookRepository.save(any())).thenReturn(bookMono);
        CommentDto comment = new CommentDto("7", "Такое себе!!!");

        webClient.post().uri("/api/comment?bookId=3")
                .header(HttpHeaders.ACCEPT, "application/json")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapper.writeValueAsString(comment))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void shouldPrintComments() throws JsonProcessingException {
        Book book = getBook();
        Mono<Book> bookMono = Mono.just(book);
        Mockito.when(bookRepository.findById("3")).thenReturn(bookMono);

        webClient.get().uri("/api/comment?bookId=3")
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void shouldDeleteComment() {
        Book book = getBook();
        Mono<Book> bookMono = Mono.just(book);
        Mockito.when(bookRepository.findById("3")).thenReturn(bookMono);
        Mockito.when(bookRepository.save(any())).thenReturn(bookMono);

        webClient.delete().uri("/api/comment/4?bookId=3")
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectStatus().isOk();
    }

    private Book getBook() {
        Book book = new Book();
        book.setId("3");
        book.setName("Приключения капитана Врунгеля");
        book.setGenre(new Genre("2", "Детектив"));
        book.setAuthorList(List.of(new Author("1", "Вася")));
        List<Comment> list = new ArrayList<>();
        list.add(new Comment("4", "Такое себе"));
        book.setCommentList(list);
        return book;
    }
}
