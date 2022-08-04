package ru.otus.spring.controller;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.dto.BookDto;
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
    public Mono<ResponseEntity<Void>> saveComment(@RequestParam(name = "bookId") String bookId, @RequestBody CommentDto comment) {
        return bookRepository.findById(bookId)
                .flatMap(book -> {
                    comment.setId(new ObjectId().toString());
                    book.getCommentList().add(CommentDto.fromDto(comment));
                    return bookRepository.save(book).thenReturn(new ResponseEntity<>(HttpStatus.OK));
                });
    }

    @DeleteMapping("/api/comment/{id}")
    public Mono<ResponseEntity<Void>> deleteComment(@RequestParam(name = "bookId") String bookId, @PathVariable(name = "id") String id) {
        return bookRepository.findById(bookId)
                .flatMap(book -> {
                    book.getCommentList().removeIf(x -> x.getId().equals(id));
                    return bookRepository.save(book).thenReturn(new ResponseEntity<>(HttpStatus.OK));
                });
    }
}
