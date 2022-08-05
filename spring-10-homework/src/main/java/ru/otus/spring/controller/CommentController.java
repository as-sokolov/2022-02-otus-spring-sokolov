package ru.otus.spring.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.dto.AuthorDto;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.service.CommentService;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@Slf4j
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @GetMapping("/api/comment")
    public List<CommentDto> listComments(@RequestParam(name = "bookId") Long bookId) {
        return commentService.getComments(bookId).stream().map(CommentDto::toDto).collect(Collectors.toList());
    }

    @DeleteMapping("/api/comment/{id}")
    public String deleteComment(@PathVariable(name = "id") Long id) {
        try {
            commentService.deleteCommentById(id);
        } catch (DataIntegrityViolationException ex) {
            log.error("CommentController.deleteComment exception ", ex);
            return String.format("При удалении комментария с id = %s произошла ошибка", id);
        }
        return "";

    }

    @PostMapping("/api/comment")
    public void saveComment(@RequestParam(name = "bookId") Long bookId, @RequestBody CommentDto comment) {
        commentService.saveComment(bookId, CommentDto.fromDto(comment));
    }
}
