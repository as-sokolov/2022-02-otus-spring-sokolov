package ru.otus.spring.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.models.Author;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Comment;
import ru.otus.spring.models.Genre;
import ru.otus.spring.service.AuthorService;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.CommentService;
import ru.otus.spring.service.GenreService;
import ru.otus.spring.view.LibraryView;

@ShellComponent
public class Spring08HomeworkApplicationShellCommands {

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final CommentService commentService;
    private final LibraryView libraryView;

    public Spring08HomeworkApplicationShellCommands(BookService bookService, AuthorService authorService, GenreService genreService, LibraryView libraryView,
                                                    CommentService commentService                                               ) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.genreService = genreService;
        this.libraryView = libraryView;
        this.commentService = commentService;
    }

    @ShellMethod(value = "Print all authors", key = {"printauthors", "pa"})
    public void printAuthors() {
        libraryView.printAllAuthors(authorService.getAllAuthors());
    }

    @ShellMethod(value = "Print all books", key = {"printbooks", "pb"})
    public void printBooks() {
        libraryView.printAllBooks(bookService.getAllBooks());
    }

    @ShellMethod(value = "Print all genres", key = {"printgenres", "pg"})
    public void printGenres() {
        libraryView.printAllGenres(genreService.getAllGenres());
    }


    @ShellMethod(value = "Print author by Id", key = {"printauthor", "paid"})
    public void printAuthor() {
        String id = libraryView.getId();
        if (null != id) {
            libraryView.printAuthor(authorService.getAuthor(id));
        }
    }

    @ShellMethod(value = "Print book by id", key = {"printbook", "pbid"})
    public void printBook() {
        String id = libraryView.getId();
        if (null != id) {
            libraryView.printBook(bookService.getBook(id));
        }
    }

    @ShellMethod(value = "Print genre by id", key = {"printgenre", "pgid"})
    public void printGenre() {
        String id = libraryView.getId();
        if (null != id) {
            libraryView.printGenre(genreService.getGenre(id));
        }
    }


    @ShellMethod(value = "Delete author by Id", key = {"deleteauthor", "daid"})
    public void deleteAuthorById() {
        String id = libraryView.getId();
        if (null != id) {
            authorService.deleteAuthor(id);
        }
    }

    @ShellMethod(value = "Delete book by id", key = {"deletebook", "dbid"})
    public void deletеBookById() {
        String id = libraryView.getId();
        if (null != id) {
            bookService.deleteBook(id);
        }
    }

    @ShellMethod(value = "Delete genre by id", key = {"deletegenre", "dgid"})
    public void deleteGenreById() {
        String id = libraryView.getId();
        if (null != id) {
            genreService.deleteGenre(id);
        }
    }

    @ShellMethod(value = "Add Genre", key = {"addgenre", "ag"})
    public void addGenre() {
        Genre genre = libraryView.getGenre();
        if (null != genre) {
            genreService.addGenre(genre);
        }
    }

    @ShellMethod(value = "Add author", key = {"addauthor", "aa"})
    public void addAuthor() {
        Author author = libraryView.getAuthor();
        if (null != author) {
            authorService.addAuthor(author);
        }
    }

    @ShellMethod(value = "Add book", key = {"addbook", "ab"})
    public void addBook() {
        Book book = libraryView.getBook();
        if (null != book) {
            bookService.addBook(book);
        }
    }

    @ShellMethod(value = "Update book", key = {"updatebook", "ub"})
    public void updateBook() {
        String id = libraryView.getId();
        Book book = libraryView.getBook();
        if (null != book) {
            book.setId(id);
            bookService.updateBook(book);
        }
    }

    @ShellMethod(value="Add comment", key = {"addcomment", "ac"})
    public void addComment() {
        String bookId = libraryView.getId();
        Comment comment = libraryView.getComment();
        if (null != comment) {
            commentService.saveComment(bookId, comment);
        }
    }

    @ShellMethod(value="Delete comment", key = {"deletecomment", "dc"})
    public void deleteComment() {
        String bookId = libraryView.getId();

        String commentId = libraryView.getId();

        if (null != bookId && null != commentId) {
            commentService.deleteCommentByBookIdAndCommentId(bookId, commentId);
        }
    }

}
