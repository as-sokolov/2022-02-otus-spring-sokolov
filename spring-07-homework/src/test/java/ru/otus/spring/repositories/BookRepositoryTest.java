package ru.otus.spring.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.spring.models.Author;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Comment;
import ru.otus.spring.models.Genre;
import java.util.ArrayList;
import java.util.List;

@DisplayName("Dao для работы с книгами должно ")
@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class BookRepositoryTest {

    private static final int EXPECTED_BOOKS_COUNT = 1;
    private static final Long EXISTING_BOOKS_ID = 1L;
    private static final String EXISTING_BOOK_NAME = "Убийство в отблесках мониторов";
    private static final String INSERT_BOOK_NAME = "Приключения Алисы в стране Spring";

    @Autowired
    private TestEntityManager em;

    @Autowired
    private BookRepository bookRepository;

    @DisplayName("возвращать ожидаемый список книг в БД")
    @Test
    @Order(1)
    void shouldReturnExpectedBooks() {
        List<Book> books = bookRepository.findAll();
        assertThat(books.size()).isEqualTo(EXPECTED_BOOKS_COUNT);
        assertThat(books.get(0)).usingRecursiveComparison().isEqualTo(getBook(true));
    }

    @DisplayName("добавлять книгу в БД")
    @Test
    @Order(2)
    void shouldInsertBook() {
        Book insertBook = getBook(false);
        Book actualBook = bookRepository.getById(bookRepository.save(insertBook).getId());
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(insertBook);
    }

    @DisplayName("возвращать ожидаемую книгу по ее id")
    @Test
    @Order(3)
    void shouldReturnExpectedBookById() {
        Book expectedBook = getBook(true);
        Book actualBook = bookRepository.getById(expectedBook.getId());
        assertThat(actualBook.getName()).isEqualTo(expectedBook.getName());
    }

    @DisplayName("обновлять заданную книгу")
    @Test
    @Order(4)
    void shouldCorrectUpdateBook() {
        Book updatedBook = getBook(false);
        Book expectedBook = bookRepository.getById(EXISTING_BOOKS_ID);
        updatedBook.setId(expectedBook.getId());
        bookRepository.save(updatedBook);
        assertThat(bookRepository.getById(EXISTING_BOOKS_ID).getName()).isEqualTo(updatedBook.getName());
    }

    @DisplayName("удалять заданную книгу по ее id")
    @Test
    @Order(5)
    void shouldCorrectDeleteBookById() {
        assertThat(bookRepository.getById(EXISTING_BOOKS_ID)).isNotNull();
        bookRepository.deleteById(EXISTING_BOOKS_ID);
        assertThat(bookRepository.existsById(EXISTING_BOOKS_ID)).isFalse();
    }



    private Book getBook(boolean isExistedBook) {
        ArrayList<Author> authors = new ArrayList<>();
        authors.add(new Author(1, "Маша"));
        authors.add(new Author(2, "Иван"));
        Book book = new Book(1, isExistedBook ? EXISTING_BOOK_NAME : INSERT_BOOK_NAME, authors, new Genre(1, "Детектив"), new ArrayList<>());
        List<Comment> commentList = new ArrayList<>();
        commentList.add(0, new Comment(1, "Книга норм", book));
        commentList.add(1, new Comment(2, "Так себе", book));
        book.getCommentList().addAll(commentList);
        return book;
    }
}
