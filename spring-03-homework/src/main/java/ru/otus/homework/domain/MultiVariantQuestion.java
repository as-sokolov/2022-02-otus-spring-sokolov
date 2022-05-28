package ru.otus.homework.domain;

import lombok.Getter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Вопрос с несколькими вариантами ответа
 */
@Getter
public class MultiVariantQuestion extends NoVariantQuestion {
    protected List<String> answers;

    public MultiVariantQuestion(String questionText, String answer, String[] answers) {
        super(questionText, answer);

        // Используем Set, чтобы исключить дубли
        Set<String> unshuffledAnswers = (answers == null) ? new HashSet<>() : new HashSet<>(Arrays.asList(answers));
        unshuffledAnswers.add(answer);
        this.answers = new ArrayList<>(unshuffledAnswers);
        // Перемешаем варианты;
        Collections.shuffle(this.answers);
    }
}
