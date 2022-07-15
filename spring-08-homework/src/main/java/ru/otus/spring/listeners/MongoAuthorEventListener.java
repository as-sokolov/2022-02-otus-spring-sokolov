package ru.otus.spring.listeners;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.data.mongodb.core.query.SerializationUtils;
import org.springframework.stereotype.Component;
import ru.otus.spring.models.Author;
import ru.otus.spring.models.Book;
import ru.otus.spring.repositories.BookRepository;
import java.util.Optional;

@Component
public class MongoAuthorEventListener extends AbstractMongoEventListener<Author> {

    BookRepository bookRepository;

    public MongoAuthorEventListener(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Author> event) {
        super.onBeforeDelete(event);
        Optional<Book> book = bookRepository.findFirstByAuthorListId(event.getDocument().get("_id").toString());
        if (book.isPresent()) {
            throw new RuntimeException("Нельзя удалить автора, пока на него существуют ссылки в книгах");
        }
    }

}
