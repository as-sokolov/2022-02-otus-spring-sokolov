package ru.otus.spring.listeners;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import ru.otus.spring.models.Author;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Genre;
import ru.otus.spring.repositories.BookRepository;
import java.util.Optional;

@Component
public class MongoGenreEventListener extends AbstractMongoEventListener<Genre> {

    BookRepository bookRepository;

    public MongoGenreEventListener(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Genre> event) {
        super.onBeforeDelete(event);
        Optional<Book> book = bookRepository.findFirstByGenreId(event.getDocument().get("_id").toString());
        if (book.isPresent()) {
            throw new RuntimeException("Нельзя удалить жанр, пока на него существуют ссылки в книгах");
        }
    }

}

