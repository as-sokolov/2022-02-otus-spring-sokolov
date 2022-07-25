package ru.otus.spring.service;

import ru.otus.spring.dto.GenreDto;
import java.util.List;

public interface GenreService {
    
    GenreDto addGenre(GenreDto genre);

    void deleteGenre(Long id);

    GenreDto getGenre(Long id);

    List<GenreDto> getAllGenres();
}
