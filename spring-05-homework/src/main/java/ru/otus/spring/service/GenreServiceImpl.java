package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.dao.GenreDao;
import ru.otus.spring.domain.Genre;
import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreDao genreDao;

    public GenreServiceImpl(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    @Override
    public Long addGenre(Genre genre) {
        return genreDao.insert(genre);
    }

    @Override
    public void deleteGenre(Long id) {
        genreDao.deleteById(id);
    }

    @Override
    public Genre getGenre(Long id) {
        return genreDao.getById(id);
    }

    public List<Genre> getAllGenres() {
        return genreDao.getAll();
    }
}
