package ru.otus.homework.domain;

import lombok.Data;

@Data
public abstract class Question {

    // Формлуировка вопроса
    protected final String questionText;

    // Пользовательский ответ
    protected String answer;

    public Question(String questionText) {
        this.questionText = questionText;
    }

    /**
     * @return Проверяем ответ и возвращаем количество начисленных баллов
     */
    public abstract int checkAnswer();
}
