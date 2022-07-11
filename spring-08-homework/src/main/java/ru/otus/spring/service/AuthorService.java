package ru.otus.spring.service;

import ru.otus.spring.models.Author;
import java.util.List;

public interface AuthorService {

    Author addAuthor(Author author);

    void deleteAuthor(String id);

    Author getAuthor(String id);

    List<Author> getAllAuthors();
}
