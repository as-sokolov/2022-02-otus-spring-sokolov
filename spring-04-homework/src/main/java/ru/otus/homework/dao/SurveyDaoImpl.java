package ru.otus.homework.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ru.otus.homework.domain.MultiVariantQuestion;
import ru.otus.homework.domain.NoScoreQuestion;
import ru.otus.homework.domain.NoVariantQuestion;
import ru.otus.homework.domain.Question;
import ru.otus.homework.domain.Survey;
import ru.otus.homework.service.IOService;
import java.util.Arrays;

@Slf4j
@Component
public class SurveyDaoImpl implements SurveyDao {

    @Value("${survey.passScore}")
    private int passScore;

    private IOService ioService;

    public SurveyDaoImpl(IOService ioService) {
        this.ioService = ioService;
    }

    /**
     * Читаем вопросы из файла и парсим в Survey
     *
     * @return Survey с вопросами или пустой, если при чтении возникла ошибка
     */
    @Override
    public Survey getSurvey() {
        Survey survey = new Survey(passScore);
        for (String[] string : ioService.read()) {
            Question question = parseQuestion(string);
            if (null != question) {
                survey.getQuestions().add(question);
            }
        }

        return survey;
    }

    /**
     * Парсинг вопроса из строчки csv файла
     *
     * @param csvLine Строчка для парсинга
     * @return объект типа Question или null, если не удалось распарсить
     */
    private Question parseQuestion(String[] csvLine) {
        if (null == csvLine || csvLine.length < 1) return null;

        switch (csvLine.length) {
            case 1:
                return new NoScoreQuestion(csvLine[0]);
            case 2:
                return new NoVariantQuestion(csvLine[0], csvLine[1]);
            default:
                return new MultiVariantQuestion(csvLine[0], csvLine[1], Arrays.copyOfRange(csvLine, 2, csvLine.length));
        }
    }

}
