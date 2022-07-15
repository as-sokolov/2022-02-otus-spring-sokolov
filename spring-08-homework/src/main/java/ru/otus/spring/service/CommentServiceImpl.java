package ru.otus.spring.service;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Comment;
import ru.otus.spring.repositories.BookRepository;

@Service
public class CommentServiceImpl implements CommentService {

    private final BookRepository bookRepository;

    public CommentServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    @Override
    @Transactional
    public Comment saveComment(String bookId, Comment comment) {
        Book book = bookRepository.findById(bookId).orElseThrow();
        if (book != null) {
            comment.setId(new ObjectId().toString());
            book.getCommentList().add(comment);
            bookRepository.save(book);
            return comment;
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteCommentByBookIdAndCommentId(String bookId, String commentId) {
        Book book = bookRepository.findById(bookId).orElseThrow();
        if (book != null) {
            if (book.getCommentList() != null) {
                book.getCommentList().removeIf(x -> x.getId().equals(commentId));
            }
            bookRepository.save(book);
        }
    }

}
