package ru.otus.spring.controller;

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
import ru.otus.spring.models.Author;
import ru.otus.spring.repositories.AuthorRepository;
import ru.otus.spring.repositories.BookRepository;
import java.util.Arrays;
import java.util.Collections;

@WebFluxTest
@ContextConfiguration(classes = AuthorController.class)
class AuthorControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private BookRepository bookRepository;


    @Test
    public void shouldPrintAuthors() throws JsonProcessingException {
        Author author = new Author("1", "Вася");
        Flux<Author> authorFlux = Flux.fromIterable(Collections.singletonList(author));
        Mockito.when(authorRepository.findAll()).thenReturn(authorFlux);
        webClient.get().uri("/api/author")
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(mapper.writeValueAsString(Collections.singletonList(author)));
    }

    @Test
    public void shouldDeleteAuthor() {
        Mockito.when(bookRepository.findFirstByAuthorListId("1")).thenReturn(Mono.empty());
        Mockito.when(authorRepository.deleteById("1")).thenReturn(Mono.just("").then());

        webClient.delete().uri("/api/author/1")
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void shouldSaveAuthor() throws JsonProcessingException {
        Author author = new Author("1", "Вася");
        Mono<Author> authorMono = Mono.just(author);
        Mockito.when(authorRepository.save(author)).thenReturn(authorMono);
        webClient.post().uri("/api/author")
                .header(HttpHeaders.ACCEPT, "application/json")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapper.writeValueAsString(author))
                .exchange()
                .expectStatus().isOk();
    }
}