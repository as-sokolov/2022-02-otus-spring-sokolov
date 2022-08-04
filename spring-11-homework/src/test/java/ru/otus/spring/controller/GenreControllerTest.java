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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.models.Genre;
import ru.otus.spring.repositories.GenreRepository;
import ru.otus.spring.repositories.BookRepository;
import java.util.Collections;

@WebFluxTest
@ContextConfiguration(classes = GenreController.class)
public class GenreControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private BookRepository bookRepository;


    @Test
    public void shouldPrintGenres() throws JsonProcessingException {
        Genre genre = new Genre("1", "Детектив");
        Flux<Genre> genreFlux = Flux.fromIterable(Collections.singletonList(genre));
        Mockito.when(genreRepository.findAll()).thenReturn(genreFlux);
        webClient.get().uri("/api/genre")
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(mapper.writeValueAsString(Collections.singletonList(genre)));
    }

    @Test
    public void shouldDeleteGenre() {
        Mockito.when(bookRepository.findFirstByGenreId("1")).thenReturn(Mono.empty());
        Mockito.when(genreRepository.deleteById("1")).thenReturn(Mono.just("").then());

        webClient.delete().uri("/api/genre/1")
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void shouldSaveGenre() throws JsonProcessingException {
        Genre genre = new Genre("1", "Детектив");
        Mono<Genre> genreMono = Mono.just(genre);
        Mockito.when(genreRepository.findFirstByName("Детектив")).thenReturn(Mono.empty());
        Mockito.when(genreRepository.save(genre)).thenReturn(genreMono);
        webClient.post().uri("/api/genre")
                .header(HttpHeaders.ACCEPT, "application/json")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapper.writeValueAsString(genre))
                .exchange()
                .expectStatus().isOk();
    }    
}
