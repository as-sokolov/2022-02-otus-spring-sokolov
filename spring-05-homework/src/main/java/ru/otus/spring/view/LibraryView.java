package ru.otus.spring.view;

import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import java.util.List;

public interface LibraryView {
    void printAllAuthors(List<Author> authorList);
    void printAllBooks(List<Book> bookList);
    void printAllGenres(List<Genre> genreList);

    void printAuthor(Author author);
    void printGenre(Genre genre);
    void printBook(Book book);

    Long getId();

    Author getAuthor();

    Genre getGenre();

    Book getBook();
}
