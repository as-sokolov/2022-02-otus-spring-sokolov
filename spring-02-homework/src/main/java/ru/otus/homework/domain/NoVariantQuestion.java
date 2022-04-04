package ru.otus.homework.domain;

import org.apache.commons.lang3.StringUtils;

/**
 * Вопрос без вариантов ответа
 */
public class NoVariantQuestion extends Question {
    protected final String correctAnswer;

    // Начисляем по 1 баллу в случае успешного ответа
    protected static final int CORRECT_SCORE = 1;

    // В случае неправильного ответа не начисляем штрафных баллов
    protected static final int INCORRECT_SCORE = 0;


    public NoVariantQuestion(String questionText, String correctAnswer) {
        super(questionText);
        this.correctAnswer = correctAnswer;
    }

    @Override
    public int checkAnswer() {
        return StringUtils.equalsIgnoreCase(correctAnswer, answer) ? CORRECT_SCORE : INCORRECT_SCORE;
    }
}
