package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.repositories.GenreRepository;
import ru.otus.spring.models.Genre;
import javax.annotation.security.RolesAllowed;
import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    @Transactional
    @RolesAllowed({"ADMIN"})
    public Genre addGenre(Genre genre) {
        return genreRepository.save(genre);
    }

    @Override
    @Transactional
    @RolesAllowed({"ADMIN"})
    public void deleteGenre(Long id) {
        genreRepository.deleteById(id);
    }

    @Override
    @RolesAllowed({"ADMIN", "PUSHKIN", "USER"})
    public Genre getGenre(Long id) {
        return genreRepository.getById(id);
    }

    @Override
    @RolesAllowed({"ADMIN", "PUSHKIN", "USER"})
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }
}
