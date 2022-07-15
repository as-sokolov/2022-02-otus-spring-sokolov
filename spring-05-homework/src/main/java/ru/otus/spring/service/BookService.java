package ru.otus.spring.service;

import ru.otus.spring.domain.Book;
import java.util.List;

public interface BookService {

    Long addBook(Book book);

    void deleteBook(Long id);

    void updateBook(Book book);

    Book getBook(Long id);

    List<Book> getAllBooks();
}
