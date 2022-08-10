package ru.otus.spring.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.spring.models.Book;
import java.util.List;
import java.util.Optional;

public interface BookRepository  extends JpaRepository<Book, Long> {
    @EntityGraph(value = "book-with-relations", type = EntityGraph.EntityGraphType.LOAD)
    List<Book> findAll();

    @EntityGraph(value = "book-with-relations", type = EntityGraph.EntityGraphType.LOAD)
    Book getBookById(@Param("id") Long id);

}
