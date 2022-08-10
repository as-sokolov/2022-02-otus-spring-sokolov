package ru.otus.spring.repositories;

import ru.otus.spring.models.Genre;
import java.util.List;

public interface GenreRepository {

    List<Genre> getAll();

    Genre save(Genre genre);

    Genre getById(long id);

    void deleteById(long id);
}
