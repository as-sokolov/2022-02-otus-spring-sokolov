package ru.otus.spring.dbinit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.spring.models.Author;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Comment;
import ru.otus.spring.models.Genre;
import ru.otus.spring.repositories.AuthorRepository;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.repositories.GenreRepository;
import java.util.List;

@Service
public class Initializer {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;


    @Value("${dbinit}")
    private boolean needInit;


    public Initializer(BookRepository bookRepository, GenreRepository genreRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
        this.authorRepository = authorRepository;
    }

    public void init() {
        if (needInit) {
            Genre genre = new Genre("2", "Детектив");
            genreRepository.save(genre).subscribe();

            Author author = new Author("1", "Вася");
            authorRepository.save(author).subscribe();

            Book book = new Book();
            book.setId("3");
            book.setName("Приключения капитана Врунгеля");
            book.setGenre(genre);
            book.setAuthorList(List.of(author));
            book.setCommentList(List.of(new Comment("4", "Такое себе")));
            bookRepository.save(book).subscribe();
        }
    }
}
