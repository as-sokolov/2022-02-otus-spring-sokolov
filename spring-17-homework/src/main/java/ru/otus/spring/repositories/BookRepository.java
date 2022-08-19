package ru.otus.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.models.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

}
