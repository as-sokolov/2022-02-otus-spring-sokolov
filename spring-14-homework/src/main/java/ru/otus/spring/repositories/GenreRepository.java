package ru.otus.spring.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.models.mongo.Author;
import ru.otus.spring.models.mongo.Genre;

public interface GenreRepository extends MongoRepository<Genre, String> {
}
