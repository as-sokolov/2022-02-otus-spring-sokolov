package ru.otus.spring.service;

import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Comment;
import java.util.List;

public interface BookService {

    void deleteBook(Long id);

    Book addBook(Book book);

    Book getBook(Long id);

    List<Book> getAllBooks();

    Book updateBook(Book book);
}
