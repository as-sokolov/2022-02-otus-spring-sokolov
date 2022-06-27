package ru.otus.spring.repositories;

import ru.otus.spring.models.Book;
import ru.otus.spring.models.Comment;
import java.util.List;

public interface BookRepository {

    List<Book> getAll();

    Book save(Book book);

    Book getById(long id);

    void deleteById(long id);

    // Операции с комментариями разместил здесь
    // т.к. по заданию это зависимая сущность
    // и в отрыве от книги не имеет смысла

    Comment saveComment(long bookId, Comment comment);

    List<Comment> getAllCommentsByBookId(long bookId);

    void deleteCommentById(long commentId);
}
