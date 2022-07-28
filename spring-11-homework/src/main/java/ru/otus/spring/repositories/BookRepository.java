package ru.otus.spring.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import ru.otus.spring.models.Book;
import java.util.Optional;

public interface BookRepository  extends ReactiveMongoRepository<Book, String> {
    Mono<Book> findFirstByGenreId(String s);

    Mono<Book> findFirstByAuthorListId(String s);
}
