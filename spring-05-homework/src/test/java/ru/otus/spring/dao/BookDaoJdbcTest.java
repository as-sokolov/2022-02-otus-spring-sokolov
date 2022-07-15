package ru.otus.spring.dao;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import java.util.ArrayList;
import java.util.List;

@DisplayName("Dao для работы с книгами должно ")
@JdbcTest
@Import({BookDaoJdbc.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookDaoJdbcTest {

    private static final int EXPECTED_BOOKS_COUNT = 1;
    private static final int EXISTING_BOOKS_ID = 1;
    private static final String EXISTING_BOOK_NAME = "Убийство в отблесках мониторов";
    private static final String INSERT_BOOK_NAME = "Приключения Алисы в стране Spring";


    @Autowired
    private BookDaoJdbc bookDao;

    @DisplayName("возвращать ожидаемый список книг в БД")
    @Test
    @Order(1)
    void shouldReturnExpectedBooks() {
        List<Book> books = bookDao.getAll();
        assertThat(books.size()).isEqualTo(EXPECTED_BOOKS_COUNT);
        assertThat(books.get(0)).usingRecursiveComparison().isEqualTo(getBook(true));
    }

    @DisplayName("добавлять книгу в БД")
    @Test
    @Order(2)
    void shouldInsertBook() {
        Book insertBook = getBook(false);
        Long id = bookDao.insert(insertBook);
        insertBook.setId(id);
        Book actualBook = bookDao.getById(id);
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(insertBook);
    }

    @DisplayName("возвращать ожидаемую книгу по ее id")
    @Test
    @Order(3)
    void shouldReturnExpectedBookById() {
        Book expectedBook = getBook(true);
        Book actualBook = bookDao.getById(expectedBook.getId());
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("обновлять заданную книгу")
    @Test
    @Order(4)
    void shouldCorrectUpdateBook() {
        Book updatedBook = getBook(false);
        Book expectedBook = bookDao.getById(EXISTING_BOOKS_ID);
        updatedBook.setId(expectedBook.getId());
        bookDao.update(updatedBook);
        assertThat(bookDao.getById(EXISTING_BOOKS_ID)).usingRecursiveComparison().isEqualTo(updatedBook);
    }

    @DisplayName("удалять заданную книгу по ее id")
    @Test
    @Order(5)
    void shouldCorrectDeleteBookById() {
        Book expectedBook = getBook(true);
        assertThat(bookDao.getById(EXISTING_BOOKS_ID)).isEqualTo(expectedBook);
        bookDao.deleteById(EXISTING_BOOKS_ID);
        assertThat(bookDao.getById(EXISTING_BOOKS_ID)).isNull();
    }



    private Book getBook(boolean isExistedBook) {
        ArrayList<Author> authors = new ArrayList<>();
        authors.add(new Author(1, "Маша"));
        return new Book(1, isExistedBook ? EXISTING_BOOK_NAME : INSERT_BOOK_NAME, authors, new Genre(1, "Детектив"));
    }
}
