package ru.otus.spring.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
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
    public Mono<String> deleteBook(@PathVariable("id") String id) {
        try {
            bookRepository.deleteById(id);
            return Mono.just("");
        } catch (Exception ex) {
            return Mono.just(String.format("При удалении книги c id = %s произошла ошибка", id));
        }
    }

    @PostMapping("/api/book")
    public Mono<BookDto> saveBook(@RequestBody BookDto bookDto) {
//        Book newBook = new Book();
//        Mono<Genre> genreMono = genreRepository.findById(bookDto.getGenre().getId());
//        Flux<Author> authorsFlux = authorRepository.findAllById(bookDto.getAuthorList().stream().map(AuthorDto::getId).collect(Collectors.toList()));
//        return Flux.zip(genreMono, authorsFlux, (genre, authors) -> {
//            newBook.setName(bookDto.getName());
//            newBook.setGenre(genre);
//            newBook.setAuthorList((List<Author>) authors);
//        })
//        return bookRepository.save(newBook).map(BookDto::toDto);
        return Mono.just(bookDto);
    }


    @PutMapping("/api/book/{id}")
    public Mono<Void> updateBook(@PathVariable("id") String id, @RequestBody BookDto bookDto) {
//        Book book = BookDto.fromDto(bookDto);
//        book.setId(id);
//
//        bookRepository.findById(id).
//                map(updateBook -> {
//                    updateBook.setName(null);
//                    updateBook.setGenre(null);
//                    updateBook.getAuthorList().clear();
//                    fillBookParams(updateBook, bookDto.getName(), bookDto.getAuthorList(), bookDto.getGenre().getId());
//                    bookRepository.save(updateBook);
//                    return Mono.empty();
//                });
        return Mono.empty();
    }
}
