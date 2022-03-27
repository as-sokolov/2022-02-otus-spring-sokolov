package ru.otus.homework.view;

import ru.otus.homework.domain.Survey;

/**
 * Отображение результатов теста
 */
public class SummaryView {
    public static void showSummary(Survey survey) {
        // Отображаем в простой форме: количество правильных ответов
        // Можно добавить свистелок (отображать дополнительно вопросы с правильными ответами)
        // и сделать красивый ASCII-интерфейс, но нет
        if (null == survey) return;
        System.out.println(String.format("Survey total score: %s", survey.getScore()));
    }
}
