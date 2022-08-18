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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.dto.AuthorDto;
import ru.otus.spring.models.Author;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Comment;
import ru.otus.spring.models.Genre;
import ru.otus.spring.repositories.AuthorRepository;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.repositories.GenreRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@WebFluxTest
@ContextConfiguration(classes = BookController.class)
public class BookControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;


    @Test
    public void shouldPrintBooks() throws JsonProcessingException {
        Book book = getBook();
        Flux<Book> bookFlux = Flux.fromIterable(Collections.singletonList(book));
        Mockito.when(bookRepository.findAll()).thenReturn(bookFlux);
        webClient.get().uri("/api/book")
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(mapper.writeValueAsString(Collections.singletonList(book)));
    }

    @Test
    public void shouldPrintBook() throws JsonProcessingException {
        Book book = getBook();
        Mono<Book> bookMono = Mono.just(book);
        Mockito.when(bookRepository.findById("3")).thenReturn(bookMono);
        webClient.get().uri("/api/book/3")
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(mapper.writeValueAsString(book));
    }


    @Test
    public void shouldDeleteBook() {
        Mockito.when(bookRepository.deleteById("3")).thenReturn(Mono.just("").then());

        webClient.delete().uri("/api/book/3")
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void shouldSaveBook() throws JsonProcessingException {
        Book book = getBook();
        Mono<Book> bookMono = Mono.just(book);
        Mockito.when(bookRepository.save(any())).thenReturn(bookMono);
        Mockito.when(genreRepository.findById("2")).thenReturn(Mono.just(new Genre("2", "Детектив")));
        Mockito.when(authorRepository.findAllById((Iterable<String>) any())).thenReturn(Flux.fromIterable(List.of(new Author("1", "Вася"))));

        webClient.post().uri("/api/book")
                .header(HttpHeaders.ACCEPT, "application/json")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapper.writeValueAsString(book))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void shouldUpdateBook() throws JsonProcessingException {
        Book book = getBook();
        Mono<Book> bookMono = Mono.just(book);
        Mockito.when(bookRepository.save(any())).thenReturn(bookMono);
        Mockito.when(bookRepository.findById("3")).thenReturn(bookMono);
        Mockito.when(genreRepository.findById("2")).thenReturn(Mono.just(new Genre("2", "Детектив")));
        Mockito.when(authorRepository.findAllById((Iterable<String>) any())).thenReturn(Flux.fromIterable(List.of(new Author("1", "Вася"))));

        webClient.put().uri("/api/book/3")
                .header(HttpHeaders.ACCEPT, "application/json")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapper.writeValueAsString(book))
                .exchange()
                .expectStatus().isOk();
    }

    private Book getBook() {
        Book book = new Book();
        book.setId("3");
        book.setName("Приключения капитана Врунгеля");
        book.setGenre(new Genre("2", "Детектив"));
        book.setAuthorList(List.of(new Author("1", "Вася")));
        book.setCommentList(List.of(new Comment("4", "Такое себе")));
        return book;
    }
}
