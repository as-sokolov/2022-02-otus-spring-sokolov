package ru.otus.spring.repositories;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.otus.spring.models.Author;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class AuthorRepositoryJpa implements AuthorRepository {

    @PersistenceContext
    private final EntityManager em;

    public AuthorRepositoryJpa(EntityManager em) {
        this.em = em;
    }


    @Override
    public Author getById(long id) {
        try {
            return em.find(Author.class, id);
        } catch (Exception ex) {
            log.debug("Запись с id {} не найдена", id);
            return null;
        }
    }

    @Override
    public List<Author> getAll() {
        try {
            // Проблемы N+1 у этой сущносит не должно быть
            return em.createQuery("select a from Author a", Author.class).getResultList();
        } catch (Exception ex) {
            log.debug("Записей не найдено");
            return new ArrayList<>();
        }
    }

    @Override
    public Author save(Author author) {
        if (author.getId() <= 0) {
            em.persist(author);
            return author;
        } else {
            return em.merge(author);
        }
    }

    @Override
    public void deleteById(long id) {
        Author author = getById(id);

        if (author != null) {
            em.remove(author);
        }
    }

}
