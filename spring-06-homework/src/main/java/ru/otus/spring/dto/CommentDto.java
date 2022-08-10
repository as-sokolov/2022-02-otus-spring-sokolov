package ru.otus.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.spring.models.Author;
import ru.otus.spring.models.Comment;


@Data
@AllArgsConstructor
public class CommentDto {
    private long id;

    private String text;

    public CommentDto() {
    }

    public CommentDto(String text) {
        this.text = text;
    }

    public static CommentDto toDto(Comment comment) {
        if (comment == null) {
            return null;
        }
        return new CommentDto(comment.getId(), comment.getText());
    }

    public static Comment fromDto(CommentDto commentDto) {
        if (commentDto == null) {
            return null;
        }
        return new Comment(commentDto.getId(), commentDto.getText(), null);
    }
}
