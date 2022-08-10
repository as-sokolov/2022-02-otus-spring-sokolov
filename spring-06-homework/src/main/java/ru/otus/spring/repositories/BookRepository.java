package ru.otus.spring.repositories;

import ru.otus.spring.models.Book;
import ru.otus.spring.models.Comment;
import java.util.List;

public interface BookRepository {

    List<Book> getAll();

    Book save(Book book);

    Book getById(long id);

    void deleteById(long id);
}
