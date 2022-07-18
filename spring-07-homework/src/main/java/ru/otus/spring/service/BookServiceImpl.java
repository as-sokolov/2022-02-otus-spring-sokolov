package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.models.Author;
import ru.otus.spring.repositories.AuthorRepository;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.models.Book;
import ru.otus.spring.repositories.GenreRepository;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;

    public BookServiceImpl(BookRepository bookRepository, GenreRepository genreRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public Book getBook(Long id) {
        return bookRepository.getById(id);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Book updateBook(Book book) {
        Book updatedBook = getBook(book.getId());
        if (updatedBook == null) {
            return null;
        }
        clearBookParams(updatedBook);
        fillBookParmas(updatedBook, book.getName(), book.getAuthorList(), book.getGenre().getId());
        bookRepository.save(updatedBook);
        return updatedBook;
    }

    @Override
    @Transactional
    public Book addBook(Book book) {
        Book newBook = new Book();
        fillBookParmas(newBook, book.getName(), book.getAuthorList(), book.getGenre().getId());
        bookRepository.save(newBook);
        return newBook;
    }

    private void fillBookParmas(Book book, String name, List<Author> authors, Long genreId) {
        for (Author author : authors) {
            book.getAuthorList().add(authorRepository.getById(author.getId()));
        }
        book.setGenre(genreRepository.getById(genreId));
        book.setName(name);
    }

    private void clearBookParams(Book book) {
        book.setName(null);
        book.setGenre(null);
        book.getAuthorList().clear();
    }
}
