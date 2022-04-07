package ru.otus.homework.view;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;
import ru.otus.homework.domain.MultiVariantQuestion;
import ru.otus.homework.domain.Question;
import ru.otus.homework.domain.Survey;
import java.util.Scanner;

/**
 * Отображение результатов теста
 */
@Component
public class SurveyViewConsole implements SurveyView {

    private static final String SURVEY_PASSED = "Congratulations! You have passed the survey";
    private static final String SURVEY_FAILED = "Your score is too low. You haven't passed the survey";
    private static final String TOTAL_SCORE = "Survey total score: %s";
    private static final String SUMMARY_CAPTION = "***Survey summary***";

    /**
     * Отображаем вопрос
     *
     * @param question
     */
    @Override
    public void askQuestion(Question question) {
        System.out.println(question.getQuestionText());

        // накладные расходы отделения View от Model
        if (question instanceof MultiVariantQuestion) {
            printVariants((MultiVariantQuestion) question);
        }

        handleAnswer(question);
    }

    @Override
    public void showSummary(Survey survey) {
        // Отображаем в простой форме: количество правильных ответов
        // Можно добавить свистелок (отображать дополнительно вопросы с правильными ответами)
        // и сделать красивый ASCII-интерфейс, но нет
        if (null == survey) return;
        System.out.println(SUMMARY_CAPTION);
        System.out.println(String.format(TOTAL_SCORE, survey.getScore()));
        System.out.println(survey.getResult() ? SURVEY_PASSED : SURVEY_FAILED);
    }

    /**
     * Обрабатываем введенный пользователем текст
     *
     * @param question
     */
    private void handleAnswer(Question question) {
        Scanner sc = new Scanner(System.in);
        boolean isParsed;
        do {
            String inputString = sc.next();
            isParsed = handleInput(question, inputString);
        } while (!isParsed);
    }

    /**
     * Для вопроса с вариантами выводим их на экран
     *
     * @param question
     */
    private void printVariants(MultiVariantQuestion question) {
        for (int i = 0; i < question.getAnswers().size(); i++) {
            System.out.println("" + (i + 1) + ": " + question.getAnswers().get(i));
        }
    }

    /**
     * Считываем ответ, проверяем на корректность сохраняем
     *
     * @param question
     * @param inputString
     * @return
     */
    private boolean handleInput(Question question, String inputString) {
        if (StringUtils.isEmpty(inputString)) {
            System.out.println("Answer shouldn't be empty");
            return false;
        }

        if ((question instanceof MultiVariantQuestion)) {
            if (!checkVariantQuestionRange(inputString, (MultiVariantQuestion) question)) {
                System.out.println(String.format("Input variant number from 1 to %s ", ((MultiVariantQuestion) question).getAnswers().size()));
                return false;
            } else {
                question.setAnswer(decodeVariantAnswer(inputString, (MultiVariantQuestion) question));
                return true;
            }
        } else {
            question.setAnswer(inputString);
            return true;
        }
    }

    /**
     * Проверка, что введенный вариант ответа попадает в возможный диапазон
     *
     * @param inputString
     * @param question
     * @return
     */
    private boolean checkVariantQuestionRange(String inputString, MultiVariantQuestion question) {
        if (!NumberUtils.isCreatable(inputString)) {
            return false;
        }
        int variantNumber = NumberUtils.toInt(inputString);
        return (variantNumber > 0 && variantNumber < question.getAnswers().size() + 1);
    }

    /**
     * Получаем ответ в текстовом виде для многовариантного вопроса
     *
     * @param inputString вариант ответа (1, 2, 3,..)
     * @param question
     * @return
     */
    private String decodeVariantAnswer(String inputString, MultiVariantQuestion question) {
        return question.getAnswers().get(NumberUtils.toInt(inputString) - 1);
    }
}
