package ru.otus.spring.service;

import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.dto.CommentDto;
import java.util.List;

public interface CommentService {
    CommentDto saveComment(long bookId, CommentDto comment);

    void deleteCommentById(long commentId);

    @Transactional(readOnly = true)
    List<CommentDto> getCommentsByBookId(long bookId);
}
