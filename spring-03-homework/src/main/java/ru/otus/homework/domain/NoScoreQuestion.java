package ru.otus.homework.domain;

/**
 * Общий вопрос, за который не начисляется балл: Имя студента, группа и т.д.
 */
public class NoScoreQuestion extends Question {

    public NoScoreQuestion(String questionText) {
        super(questionText);
    }

    @Override
    public int checkAnswer() {
        return 0;
    }
}
