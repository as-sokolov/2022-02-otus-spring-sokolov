package ru.otus.homework.domain;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class Survey {
    private List<Question> questions = new ArrayList<>();

    // Результат теста
    private int score = 0;
}
