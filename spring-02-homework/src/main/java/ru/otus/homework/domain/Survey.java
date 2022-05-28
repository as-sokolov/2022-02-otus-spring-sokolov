package ru.otus.homework.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class Survey {
    private final List<Question> questions = new ArrayList<>();

    // Результат теста
    private int score = 0;

    // Проходной балл
    private final int passScore;

    /**
     * @return Возвращаем результат прохождения теста
     */
    public boolean getResult() {
        // Если по каким-то причинам в Survey не загрузилось ни одного вопроса, считаем тест не пройденным
        return (questions.size() > 0) && (score >= passScore);
    }
}
