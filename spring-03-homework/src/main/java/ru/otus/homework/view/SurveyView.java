package ru.otus.homework.view;

import ru.otus.homework.domain.Question;
import ru.otus.homework.domain.Survey;

public interface SurveyView {

    void showSummary(Survey survey);

    void askQuestion(Question question);
}
