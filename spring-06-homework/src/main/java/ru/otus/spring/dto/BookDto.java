package ru.otus.spring.dto;

import lombok.Data;
import ru.otus.spring.models.Author;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Genre;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class BookDto {
    private long id;
    private String name;
    private List<AuthorDto> authorList = new ArrayList<>();
    private GenreDto genre;
    private List<CommentDto> commentList  = new ArrayList<>();

    public BookDto() {
    }

    public BookDto(String name, List<AuthorDto> authorList, GenreDto genre, List<CommentDto> commentList) {
        this.name = name;
        this.authorList = authorList;
        this.genre = genre;
        this.commentList = commentList;
    }


    public static BookDto toDto(Book book) {
        if (book == null) {
            return null;
        }

        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setName(book.getName());
        bookDto.setAuthorList(book.getAuthorList().stream().map(AuthorDto::toDto).collect(Collectors.toList()));
        bookDto.setGenre(GenreDto.toDto(book.getGenre()));
        bookDto.setCommentList(book.getCommentList().stream().map(CommentDto::toDto).collect(Collectors.toList()));
        return bookDto;
    }

    public static Book fromDto(BookDto bookDto) {
        if (bookDto == null) {
            return null;
        }

        Book book = new Book();
        book.setId(bookDto.getId());
        book.setName(bookDto.getName());
        book.setAuthorList(bookDto.getAuthorList().stream().map(AuthorDto::fromDto).collect(Collectors.toList()));
        book.setGenre(GenreDto.fromDto(bookDto.getGenre()));
        book.setCommentList(bookDto.getCommentList().stream().map(CommentDto::fromDto).collect(Collectors.toList()));
        return book;
    }
}
