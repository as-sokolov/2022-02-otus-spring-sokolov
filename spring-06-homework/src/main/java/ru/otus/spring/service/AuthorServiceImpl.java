package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.dto.AuthorDto;
import ru.otus.spring.repositories.AuthorRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    @Transactional
    public AuthorDto addAuthor(AuthorDto author) {
        return AuthorDto.toDto(authorRepository.save(AuthorDto.fromDto(author)));
    }

    @Override
    @Transactional
    public void deleteAuthor(Long id) {
        authorRepository.deleteById(id);
    }

    @Override
    public AuthorDto getAuthor(Long id) {
        return AuthorDto.toDto(authorRepository.getById(id));
    }

    @Override
    public List<AuthorDto> getAllAuthors() {
        return authorRepository.getAll().stream().map(AuthorDto::toDto).collect(Collectors.toList());
    }    
}
