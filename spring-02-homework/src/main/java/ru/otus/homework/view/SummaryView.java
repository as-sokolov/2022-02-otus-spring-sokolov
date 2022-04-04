package ru.otus.homework.view;

import ru.otus.homework.domain.Survey;

/**
 * Отображение результатов теста
 */
public class SummaryView {

    private static final String SURVEY_PASSED = "Congratulations! You have passed the survey";
    private static final String SURVEY_FAILED = "Your score is too low. You haven't passed the survey";
    private static final String TOTAL_SCORE= "Survey total score: %s";
    private static final String SUMMARY_CAPTION= "***Survey summary***";

    public static void showSummary(Survey survey) {
        // Отображаем в простой форме: количество правильных ответов
        // Можно добавить свистелок (отображать дополнительно вопросы с правильными ответами)
        // и сделать красивый ASCII-интерфейс, но нет
        if (null == survey) return;
        System.out.println(SUMMARY_CAPTION);
        System.out.println(String.format(TOTAL_SCORE, survey.getScore()));
        System.out.println(survey.getResult() ? SURVEY_PASSED : SURVEY_FAILED);
    }
}
