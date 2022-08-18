package ru.otus.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.models.Picture;

public interface PictureRepository extends JpaRepository<Picture, Long> {
}
