package ru.otus.spring.service;

import ru.otus.spring.models.Genre;
import java.util.List;

public interface GenreService {
    
    Genre addGenre(Genre genre);

    void deleteGenre(Long id);

    Genre getGenre(Long id);

    List<Genre> getAllGenres();
}
