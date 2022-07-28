package ru.otus.spring.controller;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.dto.CommentDto;
import ru.otus.spring.models.Book;
import ru.otus.spring.repositories.BookRepository;


@RestController
@Slf4j
public class CommentController {

    private final BookRepository bookRepository;

    public CommentController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    @GetMapping("/api/comment")
    public Flux<CommentDto> listComments(@RequestParam(name = "bookId") String bookId) {
        return bookRepository
                .findById(bookId)
                .map(Book::getCommentList)
                .flatMapIterable(list -> list)
                .map(CommentDto::toDto);
    }

    @PostMapping("/api/comment")
    public Mono<Void> saveComment(@RequestParam(name = "bookId") String bookId, @RequestBody CommentDto comment) {
        bookRepository.findById(bookId)
                .map(book -> {
                    comment.setId(new ObjectId().toString());
                    book.getCommentList().add(CommentDto.fromDto(comment));
                    bookRepository.save(book);
                    return Mono.empty();
                });
        return Mono.empty();
    }

    @DeleteMapping("/api/comment/{id}")
    public Mono<String> deleteComment(@RequestParam(name = "bookId") String bookId, @PathVariable(name = "id") String id) {
        return bookRepository.findById(bookId)
                .map(book -> {
                    book.getCommentList().removeIf(x -> x.getId().equals(id));
                    try {
                        bookRepository.save(book);
                    } catch (Exception ex) {
                        return String.format("При удалении комментария с id = %s произошла ошибка", id);
                    }
                    return "";
                });
    }
}
