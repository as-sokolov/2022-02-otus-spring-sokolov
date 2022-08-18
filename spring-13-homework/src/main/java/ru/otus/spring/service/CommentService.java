package ru.otus.spring.service;

import ru.otus.spring.models.Comment;
import java.util.List;

public interface CommentService {
    Comment saveComment(long bookId, Comment comment);

    void deleteCommentById(long commentId);

    List<Comment> getComments(Long bookId);
}
