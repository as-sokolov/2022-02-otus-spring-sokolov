package ru.otus.homework.service;

import ru.otus.homework.domain.Survey;

public interface SurveyService {

    void executeSurvey();

    Survey loadSurvey();

    void calculateScore(Survey survey);

    void askQuestions(Survey survey);

    void showSummary(Survey survey);
}
