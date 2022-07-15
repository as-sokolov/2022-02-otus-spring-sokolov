package ru.otus.spring.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import ru.otus.spring.models.Author;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Comment;
import ru.otus.spring.models.Genre;
import java.util.ArrayList;
import java.util.List;

@DisplayName("Dao для работы с книгами должно ")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataMongoTest
@TestPropertySource(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration")
public class BookRepositoryTest {

    private static final int EXPECTED_BOOKS_COUNT = 1;
    private static final String EXISTING_BOOKS_ID = "4";
    private static final String EXISTING_BOOK_NAME = "Убийство в отблесках мониторов";
    private static final String INSERT_BOOK_NAME = "Приключения Алисы в стране Spring";

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
        Book actualBook = bookRepository.findById(bookRepository.save(insertBook).getId()).orElse(null);
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(insertBook);
    }

    @DisplayName("возвращать ожидаемую книгу по ее id")
    @Test
    @Order(3)
    void shouldReturnExpectedBookById() {
        Book expectedBook = getBook(true);
        Book actualBook = bookRepository.findById(expectedBook.getId()).orElse(null);
        assertThat(actualBook.getName()).isEqualTo(expectedBook.getName());
    }

    @DisplayName("обновлять заданную книгу")
    @Test
    @Order(4)
    void shouldCorrectUpdateBook() {
        Book updatedBook = getBook(false);
        Book expectedBook = bookRepository.findById(EXISTING_BOOKS_ID).orElse(null);
        updatedBook.setId(expectedBook.getId());
        bookRepository.save(updatedBook);
        assertThat(bookRepository.findById(EXISTING_BOOKS_ID).orElse(null).getName()).isEqualTo(updatedBook.getName());
    }

    @DisplayName("удалять заданную книгу по ее id")
    @Test
    @Order(5)
    void shouldCorrectDeleteBookById() {
        assertThat(bookRepository.findById(EXISTING_BOOKS_ID).orElse(null)).isNotNull();
        bookRepository.deleteById(EXISTING_BOOKS_ID);
        assertThat(bookRepository.existsById(EXISTING_BOOKS_ID)).isFalse();
    }



    private Book getBook(boolean isExistedBook) {
        ArrayList<Author> authors = new ArrayList<>();
        authors.add(new Author("1", "Маша"));
        authors.add(new Author("2", "Иван"));
        Book book = new Book(isExistedBook ? "4" : "10", isExistedBook ? EXISTING_BOOK_NAME : INSERT_BOOK_NAME, authors, new Genre("3", "Детектив"),
                new ArrayList<>());
        List<Comment> commentList = new ArrayList<>();
        commentList.add(0, new Comment("5", "Книга норм"));
        commentList.add(1, new Comment("6", "Так себе"));
        book.getCommentList().addAll(commentList);
        return book;
    }
}
