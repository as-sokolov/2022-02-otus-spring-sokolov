package ru.otus.spring.dao;

import ru.otus.spring.domain.Author;
import java.util.List;

public interface AuthorDao {

    List<Author> getAll();

    Long insert(Author author);

    Author getById(long id);

    void deleteById(long id);
}
