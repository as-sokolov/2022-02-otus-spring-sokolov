package ru.otus.spring.mongock;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import org.bson.types.ObjectId;
import ru.otus.spring.models.Author;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Comment;
import ru.otus.spring.models.Genre;
import ru.otus.spring.repositories.AuthorRepository;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.repositories.GenreRepository;
import java.util.Arrays;

@ChangeLog
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "dropDb", author = "A.Sokolov", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertData", author = "A.Sokolov")
      public void insertBooks(BookRepository repository, AuthorRepository authorRepository, GenreRepository genreRepository) {
        Author firstAuthor = authorRepository.save(new Author("Маша"));
        Author secondAuthor = authorRepository.save(new Author("Иван"));
        Genre genre = genreRepository.save(new Genre("Детектив"));
        Book book = new Book();
        book.setName("Убийство в отблесках мониторов");
        book.setGenre(genre);
        book.setAuthorList(Arrays.asList(firstAuthor, secondAuthor));
        book.setCommentList(Arrays.asList(new Comment(new ObjectId().toString(), "Книга норм"), new Comment(new ObjectId().toString(), "Так себе")));
        repository.save(book);
    }
}
