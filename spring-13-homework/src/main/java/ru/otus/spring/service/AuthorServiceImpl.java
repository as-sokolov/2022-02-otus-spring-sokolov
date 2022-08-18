package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.repositories.AuthorRepository;
import ru.otus.spring.models.Author;
import javax.annotation.security.RolesAllowed;
import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    @Transactional
    @RolesAllowed({"ADMIN"})
    public Author addAuthor(Author author) {
        return authorRepository.save(author);
    }

    @Override
    @Transactional
    @RolesAllowed({"ADMIN"})
    public void deleteAuthor(Long id) {
        authorRepository.deleteById(id);
    }

    @Override
    @RolesAllowed({"ADMIN", "PUSHKIN", "USER"})
    public Author getAuthor(Long id) {
        return authorRepository.getById(id);
    }

    @Override
    @RolesAllowed({"ADMIN", "PUSHKIN", "USER"})
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }    
}
