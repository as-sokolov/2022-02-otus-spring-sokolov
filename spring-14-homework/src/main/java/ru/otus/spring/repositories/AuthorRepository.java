package ru.otus.spring.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.models.mongo.Author;

public interface AuthorRepository extends MongoRepository<Author, String> {

}
