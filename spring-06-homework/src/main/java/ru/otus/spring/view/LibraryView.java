package ru.otus.spring.view;

import ru.otus.spring.dto.AuthorDto;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.dto.GenreDto;
import java.util.List;

public interface LibraryView {
    void printAllAuthors(List<AuthorDto> authorList);
    void printAllBooks(List<BookDto> bookList);
    void printAllGenres(List<GenreDto> genreList);

    void printAuthor(AuthorDto author);
    void printGenre(GenreDto genre);
    void printBook(BookDto book);

    Long getId();

    AuthorDto getAuthor();

    GenreDto getGenre();

    BookDto getBook();


    void printAllComments(List<CommentDto> commentsList);

    CommentDto getComment();
}
