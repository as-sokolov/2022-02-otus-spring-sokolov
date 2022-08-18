package ru.otus.spring.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.spring.models.Book;
import ru.otus.spring.service.AuthorService;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.GenreService;
import java.util.List;

@Controller
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

    @GetMapping("/books/")
    public String listBook(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "books-list";
    }

    @GetMapping("/books/add")
    public String addBook(Model model) {
        model.addAttribute("genres", genreService.getAllGenres());
        model.addAttribute("authors", authorService.getAllAuthors());
        model.addAttribute("book", new Book());
        return "books-add";
    }

    @PostMapping("/books/add")
    public String saveBook(Book book) {
        bookService.addBook(book);
        return "redirect:/books/";
    }

    @GetMapping("/books/edit")
    public String editBook(@RequestParam(value = "id") Long id, Model model) {
        model.addAttribute("genres", genreService.getAllGenres());
        model.addAttribute("authors", authorService.getAllAuthors());
        model.addAttribute("book", bookService.getBook(id));
        return "books-edit";
    }

    @PostMapping("/books/edit")
    public String updateBook(@ModelAttribute("book") Book book) {
        bookService.updateBook(book);
        return "redirect:/books/";
    }

    @DeleteMapping("/books/delete")
    public String deleteBook(@RequestParam("id") Long id) {
        bookService.deleteBook(id);
        return "redirect:/books/";
    }
}
