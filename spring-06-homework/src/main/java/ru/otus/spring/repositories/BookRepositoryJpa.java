package ru.otus.spring.repositories;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.otus.spring.models.Author;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Comment;
import ru.otus.spring.models.Genre;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class BookRepositoryJpa implements BookRepository {


    @PersistenceContext
    private final EntityManager em;

    public BookRepositoryJpa(EntityManager em) {
        this.em = em;
    }


    @Override
    public Book getById(long id) {
        try {
            //return em.find(Book.class, id);
            TypedQuery<Book> query = em.createQuery("select distinct b from Book b join b.authorList join b.genre left join fetch b.commentList where b.id = :id",
                    Book.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (Exception ex) {
            log.error("Ошибка при выполнении запроса или записей не найдено", ex);
            return null;
        }
    }

    @Override
    public List<Book> getAll() {
        try {
            return  em.createQuery("select distinct b from Book b left join b.authorList left join b.genre left join fetch b.commentList ",
                    Book.class).getResultList();
        } catch (Exception ex) {
            log.error("Ошибка при выполнении запроса или записей не найдено", ex);
            return new ArrayList<>();
        }

    }

    @Override
    public Book save(Book book) {
        List<Author> authors = new ArrayList<>();
        for (Author author : book.getAuthorList()) {
            authors.add(em.getReference(Author.class, author.getId()));
        }
        book.getAuthorList().clear();
        book.getAuthorList().addAll(authors);
        book.setGenre(em.getReference(Genre.class, book.getGenre().getId()));

        if (book.getId() <= 0) {
            em.persist(book);
            return book;
        } else {
            List<Comment> comments = new ArrayList<>();
            for (Comment comment : book.getCommentList()) {
                if (comment.getId() <= 0) {
                    em.persist(comment);
                }
                comments.add(em.getReference(Comment.class, comment.getId()));
            }
            book.getCommentList().clear();
            book.getCommentList().addAll(comments);
            return em.merge(book);
        }
    }

    @Override
    public void deleteById(long id) {
        try {
            Book book = em.find(Book.class, id);
            em.remove(book);
        } catch (Exception ex) {
            log.debug("Запись с id {} не найдена", id);
        }
    }

    @Override
    public Comment saveComment(long bookId, Comment comment) {
        try {
            Book book = getById(bookId);

            if (book != null) {
                book.getCommentList().add(comment);
                this.save(book);
                return comment;
            }
            return null;
        } catch (Exception ex) {
            log.debug("Запись с id {} не найдена", bookId);
            return null;
        }
    }

    @Override
    public List<Comment> getAllCommentsByBookId(long id) {
        try {
            TypedQuery<Book> query = em.createQuery("select distinct b from Book b left join fetch b.commentList c where b.id = :id", Book.class);
            query.setParameter("id", id);
            return query.getSingleResult().getCommentList();
        } catch (Exception ex) {
            log.debug("Записей не найдено");
            return new ArrayList<>();
        }
    }

    @Override
    public void deleteCommentById(long id) {
        try {
            Comment comment = em.find(Comment.class, id);
            em.remove(comment);
        } catch (Exception ex) {
            log.debug("Запись с id {} не найдена", id);
       }
    }
}

