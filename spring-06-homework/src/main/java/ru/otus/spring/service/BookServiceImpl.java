package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.models.Comment;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.models.Book;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void saveBook(Book book) {
        bookRepository.save(book);
    }

    @Override
    @Transactional(readOnly = true)
    public Book getBook(Long id) {
        return bookRepository.getById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return bookRepository.getAll();
    }

    @Override
    @Transactional
    public Comment saveComment(long bookId, Comment comment) {
        return bookRepository.saveComment(bookId, comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getAllCommentsByBookId(long bookId) {
        return bookRepository.getAllCommentsByBookId(bookId);
    }

    @Override
    @Transactional
    public void deleteCommentById(long commentId) {
        bookRepository.deleteCommentById(commentId);
    }
}
