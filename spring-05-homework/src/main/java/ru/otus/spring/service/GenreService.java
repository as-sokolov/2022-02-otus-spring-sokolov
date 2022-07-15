package ru.otus.spring.service;

import ru.otus.spring.domain.Genre;
import java.util.List;

public interface GenreService {
    
    Long addGenre(Genre genre);

    void deleteGenre(Long id);

    Genre getGenre(Long id);

    List<Genre> getAllGenres();
}
