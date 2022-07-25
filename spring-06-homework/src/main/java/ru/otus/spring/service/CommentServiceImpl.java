package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Comment;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.repositories.CommentRepository;
import java.util.List;
import java.util.stream.Collectors;

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
    public CommentDto saveComment(long bookId, CommentDto commentDto) {
        Book book = bookRepository.getById(bookId);
        Comment comment = CommentDto.fromDto(commentDto);
        if (book != null) {
            comment.setBook(book);
            return CommentDto.toDto(commentRepository.saveComment(comment));
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteCommentById(long commentId) {
        commentRepository.deleteCommentById(commentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByBookId(long bookId) {
        return bookRepository.getById(bookId).getCommentList().stream().map(CommentDto::toDto).collect(Collectors.toList());
    }

}
