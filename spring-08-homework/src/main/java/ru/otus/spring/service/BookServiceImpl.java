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
    @Transactional
    public void deleteBook(String id) {
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Book getBook(String id) {
        return bookRepository.findById(id).orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional
    public Book updateBook(Book book) {
        Book updatedBook = getBook(book.getId());
        if (updatedBook == null) {
            return null;
        }
        clearBookParams(updatedBook);
        fillBookParams(updatedBook, book.getName(), book.getAuthorList(), book.getGenre().getId());
        bookRepository.save(updatedBook);
        return updatedBook;
    }

    @Override
    @Transactional
    public Book addBook(Book book) {
        Book newBook = new Book();
        fillBookParams(newBook, book.getName(), book.getAuthorList(), book.getGenre().getId());
        bookRepository.save(newBook);
        return newBook;
    }

    private void fillBookParams(Book book, String name, List<Author> authors, String genreId) {
        for (Author author : authors) {
            book.getAuthorList().add(authorRepository.findById(author.getId()).orElseThrow());
        }
        book.setGenre(genreRepository.findById(genreId).orElseThrow());
        book.setName(name);
    }

    private void clearBookParams(Book book) {
        book.setName(null);
        book.setGenre(null);
        book.getAuthorList().clear();
    }
}
