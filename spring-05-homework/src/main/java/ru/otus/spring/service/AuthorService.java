package ru.otus.spring.service;

import ru.otus.spring.domain.Author;
import java.util.List;

public interface AuthorService {

    Long addAuthor(Author author);

    void deleteAuthor(Long id);

    Author getAuthor(Long id);

    List<Author> getAllAuthors();
}
