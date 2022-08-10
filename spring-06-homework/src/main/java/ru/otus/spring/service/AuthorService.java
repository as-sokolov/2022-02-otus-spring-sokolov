package ru.otus.spring.service;

import ru.otus.spring.dto.AuthorDto;
import java.util.List;

public interface AuthorService {

    AuthorDto addAuthor(AuthorDto author);

    void deleteAuthor(Long id);

    AuthorDto getAuthor(Long id);

    List<AuthorDto> getAllAuthors();
}
