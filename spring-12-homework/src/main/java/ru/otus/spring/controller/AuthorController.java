package ru.otus.spring.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.spring.models.Author;
import ru.otus.spring.service.AuthorService;
import java.util.List;

@Controller
@Slf4j
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/authors/")
    public String listAuthor(Model model) {
        List<Author> authors = authorService.getAllAuthors();
        model.addAttribute("authors", authors);
        return "authors-list";
    }

    @GetMapping("/authors/add")
    public String addAuthor(Model model) {
        model.addAttribute("author", new Author());
        return "authors-add";
    }

    @PostMapping("/authors/add")
    public String saveAuthor(Author author) {
        authorService.addAuthor(author);
        return "redirect:/authors/";
    }


    @DeleteMapping("/authors/delete")
    public String deleteAuthor(@RequestParam("id") Long id, Model model) {
        try {
            authorService.deleteAuthor(id);
        } catch (DataIntegrityViolationException ex) {
            log.error("AuthorController.deleteAuthor exception ", ex);
            model.addAttribute("error", String.format("Нельзя удалить автора c id %s, пока он используется для одной из книг", id));
            return "error";
        }
        return "redirect:/authors/";
    }
}
