package ru.otus.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.spring.models.Genre;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenreDto {
    private long id;
    private String name;

    public static GenreDto toDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }

    public static Genre fromDto(GenreDto genreDto) {
        return new Genre(genreDto.getId(), genreDto.getName());
    }
}
