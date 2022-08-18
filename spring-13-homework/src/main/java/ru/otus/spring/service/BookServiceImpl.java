package ru.otus.spring.service;

import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.CumulativePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.models.Author;
import ru.otus.spring.models.Book;
import ru.otus.spring.repositories.AuthorRepository;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.repositories.GenreRepository;
import java.util.List;
import javax.annotation.security.RolesAllowed;


@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;
    private final MutableAclService mutableAclService;

    private final String NASHE_VSE_AUTHOR_NAME = "Пушкин";

    public BookServiceImpl(BookRepository bookRepository, GenreRepository genreRepository, AuthorRepository authorRepository, MutableAclService mutableAclService) {
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
        this.authorRepository = authorRepository;
        this.mutableAclService = mutableAclService;
    }

    @Override
    @Transactional
    @RolesAllowed({"ADMIN"})
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'READ')")
    public Book getBook(Long id) {
        return bookRepository.getBookById(id);
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject, 'READ')")
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#book, 'WRITE')")
    public Book updateBook(@Param("book") Book book) {
        Book updatedBook = getBook(book.getId());
        if (updatedBook == null) {
            return null;
        }
        clearBookParams(updatedBook);
        fillBookParams(updatedBook, book.getName(), book.getAuthorList(), book.getGenre().getId());
        bookRepository.save(updatedBook);
        return updatedBook;
    }

    @Override
    @Transactional
    @RolesAllowed({"ADMIN"})
    public Book addBook(Book book) {
        Book newBook = new Book();
        fillBookParams(newBook, book.getName(), book.getAuthorList(), book.getGenre().getId());
        addACL(bookRepository.save(newBook));
        return newBook;
    }

    private void addACL(Book book) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final Sid owner = new PrincipalSid(authentication);
        ObjectIdentity oid = new ObjectIdentityImpl(book.getClass(), book.getId());
        MutableAcl acl = mutableAclService.createAcl(oid);
        acl.setOwner(owner);
        boolean isPushkin = book.getAuthorList().stream().anyMatch(author -> NASHE_VSE_AUTHOR_NAME.equals(author.getName()));
        //read for all roles
        if (isPushkin) {
            acl.insertAce(acl.getEntries().size(), BasePermission.READ, new GrantedAuthoritySid("ROLE_PUSHKIN"), true);
        }
        acl.insertAce(acl.getEntries().size(), BasePermission.READ, new GrantedAuthoritySid("ROLE_ADMIN"), true);
        acl.insertAce(acl.getEntries().size(), BasePermission.READ, new GrantedAuthoritySid("ROLE_USER"), true);

        //write for custom roles
        acl.insertAce(acl.getEntries().size(), BasePermission.WRITE, new GrantedAuthoritySid("ROLE_ADMIN"), true);
        if (isPushkin) {
            acl.insertAce(acl.getEntries().size(), BasePermission.WRITE, new GrantedAuthoritySid("ROLE_PUSHKIN"), true);
        }

        mutableAclService.updateAcl(acl);
    }

    private void fillBookParams(Book book, String name, List<Author> authors, Long genreId) {
        for (Author author : authors) {
            book.getAuthorList().add(authorRepository.getById(author.getId()));
        }
        book.setGenre(genreRepository.getById(genreId));
        book.setName(name);
    }

    private void clearBookParams(Book book) {
        book.setName(null);
        book.setGenre(null);
        book.getAuthorList().clear();
    }
}
