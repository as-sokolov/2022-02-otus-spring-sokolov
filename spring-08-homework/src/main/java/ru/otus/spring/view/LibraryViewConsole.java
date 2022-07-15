package ru.otus.spring.view;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.otus.spring.models.Author;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Comment;
import ru.otus.spring.models.Genre;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
@Data
@Slf4j
public class LibraryViewConsole implements LibraryView {

    public static final String EMPTY_RECORD = "Пустая запись";

    @Override
    public void printAllAuthors(List<Author> authorList) {
        if (authorList == null || authorList.isEmpty()) {
            System.out.println("Пустой список авторов");
            return;
        }
        System.out.println("Cписок авторов:");
        for (Author author: authorList) {
            printAuthor(author);
        }
    }

    @Override
    public void printAllBooks(List<Book> bookList) {
        if (bookList == null || bookList.isEmpty()) {
            System.out.println("Пустой список книг");
            return;
        }
        System.out.println("Cписок книг:");
        for (Book book : bookList) {
            printBook(book);
        }
    }

    @Override
    public void printAllGenres(List<Genre> genreList) {
        if (genreList == null || genreList.isEmpty()) {
            System.out.println("Пустой список жанров");
            return;
        }
        System.out.println("Cписок жанров:");
        for (Genre genre : genreList) {
            printGenre(genre);
        }
    }

    @Override
    public String getId() {
        System.out.println("Введите id записи");
        Scanner sc = new Scanner(System.in);
        try {
            return sc.nextLine();
        } catch (Exception ex) {
            log.error("Введено некорректное значение id", ex);
            System.out.println("Введено некорректное значение id");
            return null;
        }
    }

    @Override
    public void printAuthor(Author author) {
        System.out.println(author == null ? EMPTY_RECORD : String.format("id автора: %s, ФИО автора: %s", author.getId(), author.getName()));
    }

    @Override
    public void printBook(Book book) {
        if (book == null) {
            System.out.println(EMPTY_RECORD);
            return;
        }
        System.out.println("==============");
        System.out.println(String.format("id книги: %s, Наименование книги: %s", book.getId(), book.getName()));
        System.out.print("Жанр книги: ");
        printGenre(book.getGenre());
        printAllAuthors(book.getAuthorList());
        printAllComments(book.getCommentList());
        System.out.println("==============");
    }

    @Override
    public void printGenre(Genre genre) {
        System.out.println(genre == null ? EMPTY_RECORD : String.format("id жанра: %s, Наименование жанра: %s", genre.getId(), genre.getName()));
    }


    @Override
    public Author getAuthor() {
        System.out.println("Введите имя автора");
        Scanner sc = new Scanner(System.in);
        try {
            return new Author(null, sc.nextLine());
        } catch (Exception ex) {
            log.error("Введено некорректное значение имени автора", ex);
            System.out.println("Введено некорректное значение имени автора");
            return null;
        }
    }

    @Override
    public Genre getGenre() {
        System.out.println("Введите название жанра");
        Scanner sc = new Scanner(System.in);
        try {
            return new Genre(null, sc.nextLine());
        } catch (Exception ex) {
            log.error("Введено некорректное значение названия жанра", ex);
            System.out.println("Введено некорректное значение названия жанра");
            return null;
        }
    }

    @Override
    public Book getBook() {
        try {
            System.out.println("Введите название книги");
            Scanner sc = new Scanner(System.in);
            String bookName = sc.nextLine();
            System.out.println("Введите id жанра");
            String genre_id = sc.nextLine();

            // Можно было бы в цикле, но для простоты решил через пробел, на функциональность не влияет
            System.out.println("Введите через пробел id авторов");
            String authorIds = sc.nextLine();
            String[] parsedIds = new String[]{};
            if (!StringUtils.isEmpty(authorIds)) {
                parsedIds = authorIds.split(" ");
            }
            List<Author> authorList = new ArrayList<>();

            for (String parsedId : parsedIds) {
                authorList.add(new Author(parsedId, null));
            }
            return new Book(null, bookName, authorList, new Genre(genre_id, null), new ArrayList<>());

        } catch (Exception ex) {
            log.error("Введено некорректное значение параметра", ex);
            System.out.println("Введено некорректное значение параметра");
            return null;
        }
    }

    @Override
    public void printAllComments(List<Comment> commentsList) {
        if (commentsList == null || commentsList.isEmpty()) {
            System.out.println("Пустой список комментариев");
            return;
        }
        System.out.println("Cписок комментариев:");
        for (Comment comment : commentsList) {
            printComment(comment);
        }
    }

    public void printComment(Comment comment) {
        System.out.println(comment == null ? EMPTY_RECORD : String.format("id комментария: %s, Текст комментария: %s", comment.getId(), comment.getText()));
    }

    @Override
    public Comment getComment() {
        System.out.println("Введите текст комменатрия");
        Scanner sc = new Scanner(System.in);
        try {
            return new Comment(null, sc.nextLine());
        } catch (Exception ex) {
            log.error("Введено некорректное значение комментария", ex);
            System.out.println("Введено некорректное значение комментария");
            return null;
        }
    }
}
