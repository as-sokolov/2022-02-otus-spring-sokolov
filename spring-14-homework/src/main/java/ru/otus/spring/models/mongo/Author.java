package ru.otus.spring.models.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;


@AllArgsConstructor
@Data
@NoArgsConstructor
@EqualsAndHashCode
@Document("authors")
public class Author {
    @Id
    private String id;

    private String name;

    public Author(String name) {
        this.name = name;
    }
}