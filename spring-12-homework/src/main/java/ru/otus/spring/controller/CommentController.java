package ru.otus.spring.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Comment;
import ru.otus.spring.service.CommentService;
import java.util.List;


@Controller
@Slf4j
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @GetMapping("/comments/")
    public String listComments(@RequestParam(name = "id") Long bookId, Model model) {
        List<Comment> comments = commentService.getComments(bookId);
        model.addAttribute("comments", comments);
        model.addAttribute("bookId", bookId);
        return "comments-list";
    }

    @DeleteMapping("/comments/delete")
    public String deleteComment(@RequestParam(name = "id") Long id, @RequestParam(name = "bookId") Long bookId, RedirectAttributes attributes) {
        commentService.deleteCommentById(id);
        attributes.addAttribute("id", bookId);
        return "redirect:/comments/";
    }

    @GetMapping("/comments/add")
    public String addComment(@RequestParam(name = "bookId") Long bookId, Model model) {
        Comment comment = new Comment();
        comment.setBook(new Book());
        comment.getBook().setId(bookId);
        model.addAttribute("comment", comment);
        return "comments-add";
    }

    @PostMapping("/comments/add")
    public String saveComment(Comment comment, RedirectAttributes attributes) {
        commentService.saveComment(comment.getBook().getId(), comment);
        attributes.addAttribute("id", comment.getBook().getId());
        return "redirect:/comments/";
    }


}
