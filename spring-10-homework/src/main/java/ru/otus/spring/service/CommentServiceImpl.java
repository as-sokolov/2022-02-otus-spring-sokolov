package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Comment;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.repositories.CommentRepository;
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
    public void deleteCommentById(long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<Comment> getComments(Long bookId) {
        return commentRepository.getAllByBookId(bookId);
    }

}
