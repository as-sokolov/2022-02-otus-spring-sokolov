package ru.otus.spring.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.dto.AuthorDto;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.dto.GenreDto;
import ru.otus.spring.models.Author;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Genre;
import ru.otus.spring.repositories.AuthorRepository;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.repositories.GenreRepository;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class BookController {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    public BookController(BookRepository bookRepository, AuthorRepository authorRepository, GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
    }

    @GetMapping("/api/book")
    public Flux<BookDto> listBook() {
        return bookRepository.findAll().map(BookDto::toDto);
    }

    @GetMapping("/api/book/{id}")
    public Mono<BookDto> editBook(@PathVariable(value = "id") String id) {
        return bookRepository.findById(id).map(BookDto::toDto);
    }

    @DeleteMapping("/api/book/{id}")
    public Mono<ResponseEntity<Void>> deleteBook(@PathVariable("id") String id) {
        return bookRepository.deleteById(id).thenReturn(ResponseEntity.ok().build());
    }

    @PostMapping("/api/book")
    public Mono<BookDto> saveBook(@RequestBody BookDto bookDto) {
        Mono<Genre> genreMono = genreRepository.findById(bookDto.getGenre().getId());
        Flux<Author> authorsFlux = authorRepository.findAllById(bookDto.getAuthorList().stream().map(AuthorDto::getId).collect(Collectors.toList()));
        return authorsFlux.collectList().zipWith(genreMono, (authors, genre) -> {
            Book newBook = new Book();
            newBook.setName(bookDto.getName());
            newBook.setGenre(genre);
            newBook.setAuthorList(authors);
            return newBook;
        }).flatMap(bookRepository::save).map(BookDto::toDto);
    }


    @PutMapping("/api/book/{id}")
    public Mono<BookDto> updateBook(@PathVariable("id") String id, @RequestBody BookDto bookDto) {
        Mono<Genre> genreMono = genreRepository.findById(bookDto.getGenre().getId());
        Mono<Book> bookMono = bookRepository.findById(id);
        Mono<List<Author>> authorsMono =
                authorRepository.findAllById(bookDto.getAuthorList().stream().map(AuthorDto::getId).collect(Collectors.toList())).collectList();
        return Mono.zip(authorsMono, bookMono, genreMono).flatMap(
                data -> {
                    Book updateBook = data.getT2();
                    updateBook.setId(id);
                    updateBook.setName(bookDto.getName());
                    updateBook.setGenre(data.getT3());
                    updateBook.setAuthorList(data.getT1());
                    return bookRepository.save(updateBook);
                }).map(BookDto::toDto);
    }
}
