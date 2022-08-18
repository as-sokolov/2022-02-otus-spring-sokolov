package ru.otus.spring.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Document("books")
@Data
public class Book {

    @Id
    private String id;

    private String name;

    private List<Author> authorList;

    private Genre genre;

    private List<Comment> commentList;

    public Book() {
        authorList = new ArrayList<>();
        commentList = new ArrayList<>();
    }
}
