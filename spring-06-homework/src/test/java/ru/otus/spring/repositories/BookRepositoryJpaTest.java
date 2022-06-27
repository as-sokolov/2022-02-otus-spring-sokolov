package ru.otus.spring.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
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
@Import({BookRepositoryJpa.class, AuthorRepositoryJpa.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookRepositoryJpaTest {

    private static final int EXPECTED_BOOKS_COUNT = 1;
    private static final int EXISTING_BOOKS_ID = 1;
    private static final int EXISTING_BOOKS_COMMENT_SIZE = 2;
    private static final String EXISTING_BOOK_NAME = "Убийство в отблесках мониторов";
    private static final String INSERT_BOOK_NAME = "Приключения Алисы в стране Spring";

    @Autowired
    private TestEntityManager em;

    @Autowired
    private BookRepositoryJpa bookRepositoryJpa;

    @DisplayName("возвращать ожидаемый список книг в БД")
    @Test
    @Order(1)
    void shouldReturnExpectedBooks() {
        List<Book> books = bookRepositoryJpa.getAll();
        assertThat(books.size()).isEqualTo(EXPECTED_BOOKS_COUNT);
        assertThat(books.get(0)).usingRecursiveComparison().isEqualTo(getBook(true));
    }

    @DisplayName("добавлять книгу в БД")
    @Test
    @Order(2)
    void shouldInsertBook() {
        Book insertBook = getBook(false);
        Book actualBook = bookRepositoryJpa.getById(bookRepositoryJpa.save(insertBook).getId());
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(insertBook);
    }

    @DisplayName("возвращать ожидаемую книгу по ее id")
    @Test
    @Order(3)
    void shouldReturnExpectedBookById() {
        Book expectedBook = getBook(true);
        Book actualBook = bookRepositoryJpa.getById(expectedBook.getId());
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("обновлять заданную книгу")
    @Test
    @Order(4)
    void shouldCorrectUpdateBook() {
        Book updatedBook = getBook(false);
        Book expectedBook = bookRepositoryJpa.getById(EXISTING_BOOKS_ID);
        updatedBook.setId(expectedBook.getId());
        bookRepositoryJpa.save(updatedBook);
        assertThat(bookRepositoryJpa.getById(EXISTING_BOOKS_ID)).usingRecursiveComparison().isEqualTo(updatedBook);
    }

    @DisplayName("добавлять комментарий по id книги")
    @Test
    @Order(5)
    void shouldCorrectAddCommentBookById() {
        assertThat(bookRepositoryJpa.getById(EXISTING_BOOKS_ID).getCommentList().size()).isEqualTo(EXISTING_BOOKS_COMMENT_SIZE);
        bookRepositoryJpa.saveComment(EXISTING_BOOKS_ID, new Comment(0, "Comment text"));
        assertThat(bookRepositoryJpa.getById(EXISTING_BOOKS_ID).getCommentList().size()).isEqualTo(EXISTING_BOOKS_COMMENT_SIZE + 1);
    }


    @DisplayName("удалять заданную книгу по ее id")
    @Test
    @Order(6)
    void shouldCorrectDeleteBookById() {
        assertThat(bookRepositoryJpa.getById(EXISTING_BOOKS_ID)).isNotNull();
        bookRepositoryJpa.deleteById(EXISTING_BOOKS_ID);
        assertThat(bookRepositoryJpa.getById(EXISTING_BOOKS_ID)).isNull();
    }



    private Book getBook(boolean isExistedBook) {
        ArrayList<Author> authors = new ArrayList<>();
        authors.add(new Author(1, "Маша"));
        authors.add(new Author(2, "Иван"));
        Book book = new Book(1, isExistedBook ? EXISTING_BOOK_NAME : INSERT_BOOK_NAME, authors, new Genre(1, "Детектив"), new ArrayList<>());
        List<Comment> commentList = new ArrayList<>();
        commentList.add(0, new Comment(1, "Книга норм"));
        commentList.add(1, new Comment(2, "Так себе"));
        book.getCommentList().addAll(commentList);
        return book;
    }
}
