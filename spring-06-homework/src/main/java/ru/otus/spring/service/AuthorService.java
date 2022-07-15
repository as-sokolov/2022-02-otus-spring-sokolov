package ru.otus.spring.service;

import ru.otus.spring.models.Author;
import java.util.List;

public interface AuthorService {

    Author addAuthor(Author author);

    void deleteAuthor(Long id);

    Author getAuthor(Long id);

    List<Author> getAllAuthors();
}
