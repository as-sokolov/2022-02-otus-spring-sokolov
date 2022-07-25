package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.dto.GenreDto;
import ru.otus.spring.repositories.GenreRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    @Transactional
    public GenreDto addGenre(GenreDto genre) {
        return GenreDto.toDto(genreRepository.save(GenreDto.fromDto(genre)));
    }

    @Override
    @Transactional
    public void deleteGenre(Long id) {
        genreRepository.deleteById(id);
    }

    @Override
    public GenreDto getGenre(Long id) {
        return GenreDto.toDto(genreRepository.getById(id));
    }

    @Override
    public List<GenreDto> getAllGenres() {
        return genreRepository.getAll().stream().map(GenreDto::toDto).collect(Collectors.toList());
    }
}
