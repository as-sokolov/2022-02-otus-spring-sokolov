package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.dao.BookDao;
import ru.otus.spring.domain.Book;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;

    public BookServiceImpl(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public Long addBook(Book book) {
        return bookDao.insert(book);
    }

    @Override
    public void deleteBook(Long id) {
        bookDao.deleteById(id);
    }

    @Override
    public void updateBook(Book book) {
        bookDao.update(book);
    }

    @Override
    public Book getBook(Long id) {
        return bookDao.getById(id);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookDao.getAll();
    }
}
