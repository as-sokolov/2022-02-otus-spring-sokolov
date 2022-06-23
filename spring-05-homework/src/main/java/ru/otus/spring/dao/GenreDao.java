package ru.otus.spring.dao;

import ru.otus.spring.domain.Genre;
import java.util.List;

public interface GenreDao {

    List<Genre> getAll();

    Long insert(Genre genre);

    Genre getById(long id);

    void deleteById(long id);
}
