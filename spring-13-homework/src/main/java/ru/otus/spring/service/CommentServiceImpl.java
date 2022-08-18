package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Comment;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.repositories.CommentRepository;
import javax.annotation.security.RolesAllowed;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;

    public CommentServiceImpl(BookRepository bookRepository, CommentRepository commentRepository) {
        this.bookRepository = bookRepository;
        this.commentRepository = commentRepository;
    }


    @Override
    @Transactional
    @RolesAllowed({"ADMIN"})
    public Comment saveComment(long bookId, Comment comment) {
        Book book = bookRepository.getById(bookId);
        if (book != null) {
            comment.setBook(book);
            return commentRepository.save(comment);
        }
        return null;
    }

    @Override
    @Transactional
    @RolesAllowed({"ADMIN"})
    public void deleteCommentById(long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    @RolesAllowed({"ADMIN", "PUSHKIN", "USER"})
    public List<Comment> getComments(Long bookId) {
        return commentRepository.getAllByBookId(bookId);
    }

}
