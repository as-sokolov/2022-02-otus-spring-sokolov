package ru.otus.spring.service;

import ru.otus.spring.dto.BookDto;
import java.util.List;

public interface BookService {

    void deleteBook(Long id);

    BookDto addBook(BookDto book);

    BookDto getBook(Long id);

    List<BookDto> getAllBooks();

    BookDto updateBook(BookDto book);
}
