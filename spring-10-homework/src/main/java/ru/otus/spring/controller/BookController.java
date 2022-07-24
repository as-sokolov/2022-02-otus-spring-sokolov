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
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.models.Book;
import ru.otus.spring.service.AuthorService;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.GenreService;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class BookController {

    private final BookService bookService;
    private final GenreService genreService;
    private final AuthorService authorService;

    public BookController(BookService bookService, GenreService genreService, AuthorService authorService) {
        this.bookService = bookService;
        this.genreService = genreService;
        this.authorService = authorService;
    }

    @GetMapping("/api/book")
    public List<BookDto> listBook() {
       return bookService.getAllBooks().stream().map(BookDto::toDto).collect(Collectors.toList());
    }

    @PostMapping("/api/book")
    public void saveBook(@RequestBody BookDto bookDto) {
        bookService.addBook(BookDto.fromDto(bookDto));
    }

    @GetMapping("/api/book/{id}")
    public BookDto editBook(@PathVariable(value = "id") Long id) {
        return BookDto.toDto(bookService.getBook(id));
    }

    @PutMapping("/api/book/{id}")
    public void updateBook(@PathVariable("id") Long id, @RequestBody BookDto bookDto) {
        Book book = BookDto.fromDto(bookDto);
        book.setId(id);
        bookService.updateBook(book);
    }

    @DeleteMapping("/api/book/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        try {
            bookService.deleteBook(id);
        } catch (DataIntegrityViolationException ex) {
            log.error("BookController.deleteBook exception ", ex);
            return String.format("При удалении книги c id = %s произошла ошибка", id);
        }
        return "";
    }
}
