package ru.otus.homework.view;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import ru.otus.homework.domain.Question;
import ru.otus.homework.domain.MultiVariantQuestion;
import java.util.Scanner;

/**
 * Вью для отображения вопросв и считывания ответов
 */
public class QuestionView {

    /**
     * Отображаем вопрос
     *
     * @param question
     */
    public static void askQuestion(Question question) {
        System.out.println(question.getQuestionText());

        // накладные расходы отделения View от Model
        if (question instanceof MultiVariantQuestion) {
            printVariants((MultiVariantQuestion) question);
        }

        handleAnswer(question);
    }


    /**
     * Обрабатываем введенный пользователем текст
     *
     * @param question
     */
    private static void handleAnswer(Question question) {
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
    private static void printVariants(MultiVariantQuestion question) {
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
    private static boolean handleInput(Question question, String inputString) {
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
    private static boolean checkVariantQuestionRange(String inputString, MultiVariantQuestion question) {
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
    private static String decodeVariantAnswer(String inputString, MultiVariantQuestion question) {
        return question.getAnswers().get(NumberUtils.toInt(inputString) - 1);
    }
}
