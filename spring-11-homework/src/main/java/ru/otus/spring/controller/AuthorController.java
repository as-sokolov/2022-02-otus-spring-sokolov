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
import ru.otus.spring.dto.AuthorDto;
import ru.otus.spring.repositories.AuthorRepository;
import ru.otus.spring.repositories.BookRepository;

@RestController
@Slf4j
public class AuthorController {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public AuthorController(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @GetMapping("/api/author")
    public Flux<AuthorDto> listAuthors() {
        return authorRepository.findAll().map(AuthorDto::toDto);
    }

    @PostMapping("/api/author")
    public Mono<AuthorDto> saveAuthor(@RequestBody AuthorDto author) {
        return authorRepository.save(AuthorDto.fromDto(author)).map(AuthorDto::toDto);
    }

    @DeleteMapping("/api/author/{id}")
    public Mono<ResponseEntity<Void>> deleteAuthor(@PathVariable("id") String id) {
        return bookRepository.findFirstByAuthorListId(id)
                .map(book -> new ResponseEntity<Void>(HttpStatus.BAD_REQUEST))
                .switchIfEmpty(
                        authorRepository.deleteById(id).thenReturn(ResponseEntity.ok().build())
                );
    }
}
