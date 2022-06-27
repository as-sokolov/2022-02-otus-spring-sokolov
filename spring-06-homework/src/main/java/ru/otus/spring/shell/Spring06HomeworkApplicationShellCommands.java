package ru.otus.spring.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.models.Author;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Comment;
import ru.otus.spring.models.Genre;
import ru.otus.spring.service.AuthorService;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.GenreService;
import ru.otus.spring.view.LibraryView;

@ShellComponent
public class Spring06HomeworkApplicationShellCommands {

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final LibraryView libraryView;

    public Spring06HomeworkApplicationShellCommands(BookService bookService, AuthorService authorService, GenreService genreService, LibraryView libraryView) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.genreService = genreService;
        this.libraryView = libraryView;
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
            bookService.saveBook(book);
        }
    }

    @ShellMethod(value = "Update book", key = {"updatebook", "ub"})
    public void updateBook() {
        Long id = libraryView.getId();
        Book book = libraryView.getBook();
        if (null != book) {
            book.setId(id);
            bookService.saveBook(book);
        }
    }

    @ShellMethod(value="Add comment", key = {"addcomment", "ac"})
    public void addComment() {
        Long bookId = libraryView.getId();
        Comment comment = libraryView.getComment();
        if (null != comment) {
            bookService.saveComment(bookId, comment);
        }
    }

    @ShellMethod(value="Get all book comments", key = {"printcomments", "pc"})
    public void printComments() {
        Long bookId = libraryView.getId();
        if (null != bookId) {
            libraryView.printAllComments(bookService.getAllCommentsByBookId(bookId));
        }
    }

    @ShellMethod(value="Delete comment", key = {"deletecomment", "dc"})
    public void deleteComment() {
        Long id = libraryView.getId();

        if (null != id) {
            bookService.deleteCommentById(id);
        }
    }

}
