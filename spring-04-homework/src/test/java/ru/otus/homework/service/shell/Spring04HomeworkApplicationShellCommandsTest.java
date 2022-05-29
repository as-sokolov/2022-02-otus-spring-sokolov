package ru.otus.homework.service.shell;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.CommandNotCurrentlyAvailable;
import org.springframework.shell.Shell;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.homework.domain.Survey;
import ru.otus.homework.service.SurveyService;
import ru.otus.homework.shell.Spring04HomeworkApplicationShellCommands;
import ru.otus.homework.shell.SurveyState;
import java.lang.reflect.Field;

@DisplayName("Тест команд shell ")
@SpringBootTest
public class Spring04HomeworkApplicationShellCommandsTest {

    @Autowired
    private Shell shell;

    private static final String COMMAND_LOAD = "load";

    private static final String COMMAND_ASK = "ask";
    private static final String COMMAND_EXECUTE = "execute";

    private static final String COMMAND_CALC = "calc";
    private static final String COMMAND_CALCULATE = "calculate";

    private static final String COMMAND_SHOW = "show";
    private static final String COMMAND_SUMMARY = "summary";
    private static final String COMMAND_SCORE = "score";

    private static final int passScore = 3;

    @MockBean
    SurveyService surveyService;

    @DisplayName(" должен загружать тест")
    @Test
    void shouldReturnSurveyAfterLoadCommandEvaluated() {
        shell.evaluate(() -> COMMAND_LOAD);
        verify(surveyService, times(1)).loadSurvey();
    }

    @DisplayName(" должен возвращать CommandNotCurrentlyAvailable при попытке выполнения команды проведения теста, подсчета баллов или вывода " +
            "результатов, если не загружен тест")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldReturnCommandNotCurrentlyAvailableObjectWhenSurveyNotLoadedAfterCommandsEvaluated() {
        Object res =  shell.evaluate(() -> COMMAND_ASK);
        assertThat(res).isInstanceOf(CommandNotCurrentlyAvailable.class);

        res =  shell.evaluate(() -> COMMAND_EXECUTE);
        assertThat(res).isInstanceOf(CommandNotCurrentlyAvailable.class);

        res =  shell.evaluate(() -> COMMAND_ASK);
        assertThat(res).isInstanceOf(CommandNotCurrentlyAvailable.class);

        res =  shell.evaluate(() -> COMMAND_CALC);
        assertThat(res).isInstanceOf(CommandNotCurrentlyAvailable.class);

        res =  shell.evaluate(() -> COMMAND_CALCULATE);
        assertThat(res).isInstanceOf(CommandNotCurrentlyAvailable.class);

        res =  shell.evaluate(() -> COMMAND_SHOW);
        assertThat(res).isInstanceOf(CommandNotCurrentlyAvailable.class);

        res =  shell.evaluate(() -> COMMAND_SUMMARY);
        assertThat(res).isInstanceOf(CommandNotCurrentlyAvailable.class);

        res =  shell.evaluate(() -> COMMAND_SCORE);
        assertThat(res).isInstanceOf(CommandNotCurrentlyAvailable.class);

        // Должны иметь возможность выполнить загрузку теста
        shell.evaluate(() -> COMMAND_LOAD);

        verify(surveyService, times(1)).loadSurvey();
        verify(surveyService, times(0)).askQuestions(Mockito.any());
        verify(surveyService, times(0)).calculateScore(Mockito.any());
        verify(surveyService, times(0)).showSummary(Mockito.any());
    }

    @DisplayName(" должен возвращать CommandNotCurrentlyAvailable при попытке выполнения команды подсчета баллов и вывода результатов," +
            " если не загружен тест и не проведен тест")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldReturnCommandNotCurrentlyAvailableObjectWhenSurveyNotExecutedAfterCommandsEvaluated() {
        Survey survey = new Survey(passScore);
        Mockito.when(surveyService.loadSurvey()).thenReturn(survey);

        shell.evaluate(() -> COMMAND_LOAD);

        // пока не выполнили тест, эти команды недоступны
        Object res =  shell.evaluate(() -> COMMAND_CALCULATE);
        assertThat(res).isInstanceOf(CommandNotCurrentlyAvailable.class);

        res =  shell.evaluate(() -> COMMAND_SCORE);
        assertThat(res).isInstanceOf(CommandNotCurrentlyAvailable.class);


        shell.evaluate(() -> COMMAND_ASK);

        // не можем запустить повторно
        res =  shell.evaluate(() -> COMMAND_ASK);
        assertThat(res).isInstanceOf(CommandNotCurrentlyAvailable.class);

        // Должны иметь возможность вернуться в начало теста
        shell.evaluate(() -> COMMAND_LOAD);

        verify(surveyService, times(2)).loadSurvey();
        verify(surveyService, times(1)).askQuestions(Mockito.any());
        verify(surveyService, times(0)).calculateScore(Mockito.any());
        verify(surveyService, times(0)).showSummary(Mockito.any());
    }

    @DisplayName(" должен возвращать CommandNotCurrentlyAvailable при попытке выполнения команды вывода результатов," +
            " если не загружен тест и не проведен тест, а также не подсчитаны результаты")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldReturnCommandNotCurrentlyAvailableObjectWhenScoreNotCalculatedAfterCommandsEvaluated() {
        Survey survey = new Survey(passScore);
        Mockito.when(surveyService.loadSurvey()).thenReturn(survey);

        shell.evaluate(() -> COMMAND_LOAD);
        shell.evaluate(() -> COMMAND_ASK);

        // эти команды недоступны
        Object res =  shell.evaluate(() -> COMMAND_SCORE);
        assertThat(res).isInstanceOf(CommandNotCurrentlyAvailable.class);

        res =  shell.evaluate(() -> COMMAND_ASK);
        assertThat(res).isInstanceOf(CommandNotCurrentlyAvailable.class);

        // а эта доступна
        shell.evaluate(() -> COMMAND_CALCULATE);

        // Должны иметь возможность вернуться в начало теста
        shell.evaluate(() -> COMMAND_LOAD);

        verify(surveyService, times(2)).loadSurvey();
        verify(surveyService, times(1)).askQuestions(Mockito.any());
        verify(surveyService, times(1)).calculateScore(Mockito.any());
        verify(surveyService, times(0)).showSummary(Mockito.any());
    }

    @DisplayName(" должен проходить стандартный цикл выполнения программы")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldExecuteSuccessPathAfterCommandsEvaluated() {
        Survey survey = new Survey(passScore);
        Mockito.when(surveyService.loadSurvey()).thenReturn(survey);

        shell.evaluate(() -> COMMAND_LOAD);
        shell.evaluate(() -> COMMAND_ASK);
        shell.evaluate(() -> COMMAND_CALCULATE);

        // эти команды недоступны
        Object res =  shell.evaluate(() -> COMMAND_ASK);
        assertThat(res).isInstanceOf(CommandNotCurrentlyAvailable.class);

        res =  shell.evaluate(() -> COMMAND_CALCULATE);
        assertThat(res).isInstanceOf(CommandNotCurrentlyAvailable.class);

        shell.evaluate(() -> COMMAND_SCORE);

        // Должны иметь возможность вернуться в начало теста
        shell.evaluate(() -> COMMAND_LOAD);

        verify(surveyService, times(2)).loadSurvey();
        verify(surveyService, times(1)).askQuestions(Mockito.any());
        verify(surveyService, times(1)).calculateScore(Mockito.any());
        verify(surveyService, times(1)).showSummary(Mockito.any());
    }
}

