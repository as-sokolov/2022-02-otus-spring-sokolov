package ru.otus.spring.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.models.Genre;
import ru.otus.spring.service.GenreService;
import java.util.List;

@RestController
@Slf4j
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/api/genre")
    public List<Genre> listGenre() {
        return genreService.getAllGenres();
    }

    @PostMapping("/api/genre")
    public String saveGenre(Genre genre, Model model) {
        try {
            genreService.addGenre(genre);
        } catch (DataIntegrityViolationException ex) {
            log.error("GenreController.saveGenre exception ", ex);
            model.addAttribute("error", String.format("Жанр с наименованием %s уже существует ", genre.getName()));
            return "error";
        }

        return "redirect:/genres/";
    }


    @DeleteMapping("/api/genre")
    public String deleteGenre(@RequestParam("id") Long id, Model model) {
        try {
            genreService.deleteGenre(id);
        } catch (DataIntegrityViolationException ex) {
            log.error("GenreController.deleteGenre exception ", ex);
            model.addAttribute("error", String.format("Нельзя удалить жанр c id %s, пока он используется для одной из книг", id));
            return "error";
        }
        return "redirect:/genres/";
    }
}
