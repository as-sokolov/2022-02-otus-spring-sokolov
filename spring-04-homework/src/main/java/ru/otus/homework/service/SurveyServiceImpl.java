package ru.otus.homework.service;

import org.springframework.stereotype.Service;
import ru.otus.homework.dao.SurveyDao;
import ru.otus.homework.domain.Question;
import ru.otus.homework.domain.Survey;
import ru.otus.homework.view.SurveyView;

@Service
public class SurveyServiceImpl implements SurveyService {
    private final SurveyDao dao;

    private final SurveyView surveyView;

    public SurveyServiceImpl(SurveyDao dao, SurveyView surveyView) {
        this.dao = dao;
        this.surveyView = surveyView;
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
     * Загружаем тест из файла
     *
     * @return Survey c тестом
     */
    @Override
    public Survey loadSurvey() {
        return dao.getSurvey();
    }

    /**
     * Подсчет правильным ответов и итогового балла
     *
     * @param survey экземпляр теста для подсчета
     */
    @Override
    public void calculateScore(Survey survey) {
        int totalScore = 0;
        for (Question question : survey.getQuestions()) {
            totalScore += question.checkAnswer();
        }
        survey.setScore(totalScore);
    }

    /**
     * Отображаем вопросы теста, считываем ответы
     *
     * @param survey
     */
    @Override
    public void askQuestions(Survey survey) {
        for (Question question : survey.getQuestions()) {
            surveyView.askQuestion(question);
        }
    }

    /**
     * Отображаем результаты теста
     *
     * @param survey
     */
    @Override
    public void showSummary(Survey survey) {
        surveyView.showSummary(survey);
    }
}
