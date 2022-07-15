package ru.otus.spring.service;

import ru.otus.spring.models.Comment;
import java.util.List;

public interface CommentService {
    Comment saveComment(String bookId, Comment comment);

    void deleteCommentByBookIdAndCommentId(String bookId, String commentId);
}
