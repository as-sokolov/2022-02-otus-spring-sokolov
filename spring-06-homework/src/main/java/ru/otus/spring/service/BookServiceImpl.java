package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.dto.AuthorDto;
import ru.otus.spring.dto.BookDto;
import ru.otus.spring.repositories.AuthorRepository;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.models.Book;
import ru.otus.spring.repositories.GenreRepository;
import java.util.List;
import java.util.stream.Collectors;

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
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public BookDto getBook(Long id) {
        return BookDto.toDto(bookRepository.getById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> getAllBooks() {
        return bookRepository.getAll().stream().map(BookDto::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookDto updateBook(BookDto book) {
        Book updatedBook = BookDto.fromDto(getBook(book.getId()));
        if (updatedBook == null) {
            return null;
        }
        clearBookParams(updatedBook);
        fillBookParmas(updatedBook, book.getName(), book.getAuthorList(), book.getGenre().getId());
        bookRepository.save(updatedBook);
        return BookDto.toDto(updatedBook);
    }

    @Override
    @Transactional
    public BookDto addBook(BookDto book) {
        Book newBook = new Book();
        fillBookParmas(newBook, book.getName(), book.getAuthorList(), book.getGenre().getId());
        bookRepository.save(newBook);
        return BookDto.toDto(newBook);
    }

    private void fillBookParmas(Book book, String name, List<AuthorDto> authors, Long genreId) {
        for (AuthorDto author : authors) {
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
