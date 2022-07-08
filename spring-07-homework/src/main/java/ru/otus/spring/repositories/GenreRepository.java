package ru.otus.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.models.Genre;
import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
