package ru.otus.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class Book {
    private long id;
    private final String name;
    private final List<Author> authorList;
    private final Genre genre;
}
