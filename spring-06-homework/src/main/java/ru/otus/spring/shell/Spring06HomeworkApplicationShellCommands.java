package ru.otus.spring.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.dto.AuthorDto;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.dto.GenreDto;
import ru.otus.spring.service.AuthorService;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.CommentService;
import ru.otus.spring.service.GenreService;
import ru.otus.spring.view.LibraryView;

@ShellComponent
public class Spring06HomeworkApplicationShellCommands {

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final CommentService commentService;
    private final LibraryView libraryView;

    public Spring06HomeworkApplicationShellCommands(BookService bookService, AuthorService authorService, GenreService genreService, LibraryView libraryView,
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
        Long id = libraryView.getId();
        if (null != id) {
            libraryView.printAuthor(authorService.getAuthor(id));
        }
    }

    @ShellMethod(value = "Print book by id", key = {"printbook", "pbid"})
    public void printBook() {
        Long id = libraryView.getId();
        if (null != id) {
            libraryView.printBook(bookService.getBook(id));
        }
    }

    @ShellMethod(value = "Print genre by id", key = {"printgenre", "pgid"})
    public void printGenre() {
        Long id = libraryView.getId();
        if (null != id) {
            libraryView.printGenre(genreService.getGenre(id));
        }
    }


    @ShellMethod(value = "Delete author by Id", key = {"deleteauthor", "daid"})
    public void deleteAuthorById() {
        Long id = libraryView.getId();
        if (null != id) {
            authorService.deleteAuthor(id);
        }
    }

    @ShellMethod(value = "Delete book by id", key = {"deletebook", "dbid"})
    public void deletеBookById() {
        Long id = libraryView.getId();
        if (null != id) {
            bookService.deleteBook(id);
        }
    }

    @ShellMethod(value = "Delete genre by id", key = {"deletegenre", "dgid"})
    public void deleteGenreById() {
        Long id = libraryView.getId();
        if (null != id) {
            genreService.deleteGenre(id);
        }
    }

    @ShellMethod(value = "Add Genre", key = {"addgenre", "ag"})
    public void addGenre() {
        GenreDto genre = libraryView.getGenre();
        if (null != genre) {
            genreService.addGenre(genre);
        }
    }

    @ShellMethod(value = "Add author", key = {"addauthor", "aa"})
    public void addAuthor() {
        AuthorDto author = libraryView.getAuthor();
        if (null != author) {
            authorService.addAuthor(author);
        }
    }

    @ShellMethod(value = "Add book", key = {"addbook", "ab"})
    public void addBook() {
        BookDto book = libraryView.getBook();
        if (null != book) {
            bookService.addBook(book);
        }
    }

    @ShellMethod(value = "Update book", key = {"updatebook", "ub"})
    public void updateBook() {
        Long id = libraryView.getId();
        BookDto book = libraryView.getBook();
        if (null != book) {
            book.setId(id);
            bookService.updateBook(book);
        }
    }

    @ShellMethod(value="Add comment", key = {"addcomment", "ac"})
    public void addComment() {
        Long bookId = libraryView.getId();
        CommentDto comment = libraryView.getComment();
        if (null != comment) {
            commentService.saveComment(bookId, comment);
        }
    }

    @ShellMethod(value="Delete comment", key = {"deletecomment", "dc"})
    public void deleteComment() {
        Long id = libraryView.getId();

        if (null != id) {
            commentService.deleteCommentById(id);
        }
    }

    @ShellMethod(value="Get comment by book id", key = {"printcomments", "pcbid"})
    public void printComments() {
        Long id = libraryView.getId();

        if (null != id) {
            libraryView.printAllComments(commentService.getCommentsByBookId(id));
        }
    }

}
