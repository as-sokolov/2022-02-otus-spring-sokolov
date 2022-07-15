package ru.otus.spring.repositories;

import ru.otus.spring.models.Author;
import java.util.List;

public interface AuthorRepository {

    List<Author> getAll();

    Author save(Author author);

    Author getById(long id);

    void deleteById(long id);
}
