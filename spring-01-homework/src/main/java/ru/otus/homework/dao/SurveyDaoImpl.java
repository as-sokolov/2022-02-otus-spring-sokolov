package ru.otus.homework.dao;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import ru.otus.homework.domain.NoScoreQuestion;
import ru.otus.homework.domain.Question;
import ru.otus.homework.domain.Survey;
import ru.otus.homework.domain.NoVariantQuestion;
import ru.otus.homework.domain.MultiVariantQuestion;
import ru.otus.homework.helper.CSVHelper;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
public class SurveyDaoImpl implements SurveyDao {

    private final Resource surveyResource;

    public SurveyDaoImpl(Resource surveyResource) {
        this.surveyResource = surveyResource;
    }

    /** Читаем вопросы из файла и парсим в Survey
     * @return Survey с вопросами или пустой, если при чтении возникла ошибка
     */
    @Override
    public Survey getSurvey() {
        Survey survey = new Survey();
        if (null == surveyResource) {
            // возвращаем пустой Survey
            return survey;
        }

        for (String[] string : CSVHelper.read(surveyResource)) {
            Question question = parseQuestion(string);
            if (null != question) {
                survey.getQuestions().add(question);
            }
        }

        return survey;
    }

    /** Парсинг вопроса из строчки csv файла
     * @param csvLine Строчка для парсинга
     * @return объект типа Question или null, если не удалось распарсить
     */
    private Question parseQuestion(String[] csvLine) {
        if (null == csvLine || csvLine.length < 1) return null;

        switch (csvLine.length) {
            case 1 :
                return new NoScoreQuestion(csvLine[0]);
            case 2 :
                return new NoVariantQuestion(csvLine[0], csvLine[1]);
            default:
                return new MultiVariantQuestion(csvLine[0], csvLine[1], Arrays.copyOfRange(csvLine, 2, csvLine.length));
        }
    }

}
