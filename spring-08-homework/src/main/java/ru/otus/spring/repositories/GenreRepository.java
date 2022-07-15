package ru.otus.spring.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.models.Author;
import ru.otus.spring.models.Genre;

public interface GenreRepository extends MongoRepository<Genre, String> {
}
