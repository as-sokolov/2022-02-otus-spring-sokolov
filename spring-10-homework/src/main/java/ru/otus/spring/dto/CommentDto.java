package ru.otus.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.spring.models.Author;
import ru.otus.spring.models.Comment;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private long id;

    private String text;

    public static CommentDto toDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText());
    }

    public static Comment fromDto(CommentDto commentDto) {
        return new Comment(commentDto.getId(), commentDto.getText(), null);
    }
}
