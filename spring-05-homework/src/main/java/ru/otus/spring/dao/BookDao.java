package ru.otus.spring.dao;

import ru.otus.spring.domain.Book;
import java.util.List;

public interface BookDao {

    List<Book> getAll();

    Long insert(Book book);

    Book getById(long id);

    void deleteById(long id);

    void update(Book book);
}
