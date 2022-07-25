package ru.otus.spring.view;

import ru.otus.spring.models.Author;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Comment;
import ru.otus.spring.models.Genre;
import java.util.List;

public interface LibraryView {
    void printAllAuthors(List<Author> authorList);
    void printAllBooks(List<Book> bookList);
    void printAllGenres(List<Genre> genreList);

    void printAuthor(Author author);
    void printGenre(Genre genre);
    void printBook(Book book);

    String getId();

    Author getAuthor();

    Genre getGenre();

    Book getBook();


    void printAllComments(List<Comment> commentsList);

    Comment getComment();
}
