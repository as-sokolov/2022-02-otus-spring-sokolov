package ru.otus.spring.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.dto.GenreDto;
import ru.otus.spring.models.Genre;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.repositories.GenreRepository;

@RestController
@Slf4j
public class GenreController {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;

    public GenreController(BookRepository bookRepository, GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
    }

    @GetMapping("/api/genre")
    public Flux<GenreDto> listGenres() {
        return genreRepository.findAll().map(GenreDto::toDto);
    }

    @PostMapping("/api/genre")
    public Mono<ResponseEntity<GenreDto>> saveGenre(@RequestBody GenreDto genreDto) {
        return genreRepository.findFirstByName(genreDto.getName())
                .map(GenreDto::toDto)
                .map(res -> ResponseEntity.badRequest().body(res))
                .switchIfEmpty(
                        genreRepository.save(GenreDto.fromDto(genreDto))
                                .map(GenreDto::toDto)
                                .map(res -> ResponseEntity.ok().body(res))
                );


    }

    @DeleteMapping("/api/genre/{id}")
    public Mono<ResponseEntity<Void>> deleteGenre(@PathVariable("id") String id) {
        return bookRepository.findFirstByGenreId(id)
                .map(res -> new ResponseEntity<Void>(HttpStatus.BAD_REQUEST))
                .switchIfEmpty(
                        genreRepository.deleteById(id).thenReturn(ResponseEntity.ok().build())
                );
    }
}
