package ru.otus.homework.service;

import ru.otus.homework.dao.SurveyDao;
import ru.otus.homework.domain.Question;
import ru.otus.homework.domain.Survey;
import ru.otus.homework.view.QuestionView;
import ru.otus.homework.view.SummaryView;

public class SurveyServiceImpl implements SurveyService {
    protected SurveyDao dao;

    public SurveyServiceImpl(SurveyDao dao) {
        this.dao = dao;
    }

    /**
     * Загружаем тест из файла
     *
     * @return Survey c тестом
     */
    private Survey loadSurvey() {
        return dao.getSurvey();
    }

    /**
     * Подсчет правильным ответов и итогового балла
     *
     * @param survey экземпляр теста для подсчета
     */
    private void calculateScore(Survey survey) {
        int totalScore = 0;
        for (Question question : survey.getQuestions()) {
            totalScore += question.checkAnswer();
        }
        survey.setScore(totalScore);
    }

    /**
     * Полный цикл проведения теста: загрузка, отображение, подсчет и вывод результатов
     */
    @Override
    public void executeSurvey() {
        // Загружаем тест из файла
        Survey survey = loadSurvey();

        // Отображаем вопросы, считываем ответы
        askQuestions(survey);

        // Подсчитываем баллы
        calculateScore(survey);

        // Отображаем результаты
        showSummary(survey);
    }

    /**
     * Отображаем вопросы теста, считываем ответы
     *
     * @param survey
     */
    private void askQuestions(Survey survey) {
        for (Question question : survey.getQuestions()) {
            QuestionView.askQuestion(question);
        }
    }

    /**
     * Отображаем результаты теста
     *
     * @param survey
     */
    private void showSummary(Survey survey) {
        SummaryView.showSummary(survey);
    }
}
