package ru.otus.spring.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.models.Book;
import java.util.Optional;

public interface BookRepository  extends MongoRepository<Book, String> {
    Optional<Book> findFirstByGenreId(String s);

    Optional<Book> findFirstByAuthorListId(String s);
}
