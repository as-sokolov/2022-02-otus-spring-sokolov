package ru.otus.spring.models.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Document("comments")
public class Comment {
    @Id
    private String id;

    private String text;

    public Comment(String text) {
        this.text = text;
    }
}
