package ru.otus.spring.repositories;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.spring.models.Genre;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@Slf4j
public class GenreRepositoryJpa implements GenreRepository {

    @PersistenceContext
    private final EntityManager em;

    public GenreRepositoryJpa(EntityManager em) {
        this.em = em;
    }

    @Override
    public Genre getById(long id) {
            return em.find(Genre.class, id);
    }

    @Override
    public List<Genre> getAll() {
            return em.createQuery("select g from Genre g", Genre.class).getResultList();
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
