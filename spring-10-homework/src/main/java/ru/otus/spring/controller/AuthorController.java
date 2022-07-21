package ru.otus.spring.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.dto.AuthorDto;
import ru.otus.spring.service.AuthorService;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/api/author")
    public List<AuthorDto> listAuthor() {
        return authorService.getAllAuthors().stream().map(AuthorDto::toDto).collect(Collectors.toList());
    }

    @PostMapping("/api/author")
    public void saveAuthor(@RequestBody AuthorDto authorDto) {
        authorService.addAuthor(AuthorDto.fromDto(authorDto));
    }


    @DeleteMapping("/api/author/{id}")
    public String deleteAuthor(@PathVariable("id") Long id) {
        try {
            authorService.deleteAuthor(id);
        } catch (DataIntegrityViolationException ex) {
            log.error("AuthorController.deleteAuthor exception ", ex);
            return String.format("Нельзя удалить автора c id %s, пока он используется для одной из книг", id);
        }
        return "";
    }
}
