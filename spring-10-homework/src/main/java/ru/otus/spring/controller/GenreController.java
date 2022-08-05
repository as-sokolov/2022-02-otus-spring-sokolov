package ru.otus.spring.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.dto.GenreDto;
import ru.otus.spring.models.Genre;
import ru.otus.spring.service.GenreService;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/api/genre")
    public List<GenreDto> listGenre() {
        return genreService.getAllGenres().stream().map(GenreDto::toDto).collect(Collectors.toList());
    }

    @PostMapping("/api/genre")
    public String saveGenre(@RequestBody GenreDto genreDto) {
        try {
            genreService.addGenre(GenreDto.fromDto(genreDto));
        } catch (DataIntegrityViolationException ex) {
            log.error("GenreController.saveGenre exception ", ex);
            return String.format("Жанр с наименованием %s уже существует ", genreDto.getName());
        }
        return "";
    }


    @DeleteMapping("/api/genre/{id}")
    public String deleteGenre(@PathVariable("id") Long id) {
        try {
            genreService.deleteGenre(id);
        } catch (DataIntegrityViolationException ex) {
            log.error("GenreController.deleteGenre exception ", ex);
            return String.format("Нельзя удалить жанр c id %s, пока он используется для одной из книг", id);
        }
        return "";
    }
}
