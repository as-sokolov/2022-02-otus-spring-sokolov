package ru.otus.spring.repositories;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.otus.spring.models.Genre;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class GenreRepositoryJpa implements GenreRepository {

    @PersistenceContext
    private final EntityManager em;

    public GenreRepositoryJpa(EntityManager em) {
        this.em = em;
    }

    @Override
    public Genre getById(long id) {
        try {
            return em.find(Genre.class, id);
        } catch (Exception ex) {
            log.debug("Запись с id {} не найдена", id);
            return null;
        }
    }

    @Override
    public List<Genre> getAll() {
        try {
            // Проблемы N+1 у этой сущносит не должно быть
            return em.createQuery("select g from Genre g", Genre.class).getResultList();
        } catch (Exception ex) {
            log.debug("Записей не найдено");
            return new ArrayList<>();
        }
    }

    @Override
    public Genre save(Genre genre) {
        if (genre.getId() <= 0) {
            em.persist(genre);
            return genre;
        } else {
            return em.merge(genre);
        }
    }

    @Override
    public void deleteById(long id) {
        Genre genre = getById(id);

        if (genre != null) {
            em.remove(genre);
        }
    }
}
