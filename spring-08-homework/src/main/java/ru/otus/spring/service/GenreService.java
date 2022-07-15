package ru.otus.spring.service;

import ru.otus.spring.models.Genre;
import java.util.List;

public interface GenreService {
    
    Genre addGenre(Genre genre);

    void deleteGenre(String id);

    Genre getGenre(String id);

    List<Genre> getAllGenres();
}
