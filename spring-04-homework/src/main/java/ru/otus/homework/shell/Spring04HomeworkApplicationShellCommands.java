package ru.otus.homework.shell;

import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.homework.domain.Survey;
import ru.otus.homework.service.SurveyService;

@ShellComponent
public class Spring04HomeworkApplicationShellCommands {

    private final SurveyService surveyService;
    private SurveyState state = SurveyState.INIT;
    private Survey survey;

    public Spring04HomeworkApplicationShellCommands(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @ShellMethod(value = "Load survey", key = {"load"})
    public void loadSurvey() {
        survey = surveyService.loadSurvey();
        state = SurveyState.LOADED;
    }

    @ShellMethod(value = "Ask questions", key = {"ask", "execute"})
    @ShellMethodAvailability(value = "isAskQuestionsCommandAvailable")
    public void askQuestions() {
        surveyService.askQuestions(survey);
        state = SurveyState.EXECUTED;
    }

    @ShellMethod(value = "Calculate score", key = {"calc", "calculate"})
    @ShellMethodAvailability(value = "isCalculateScoreCommandAvailable")
    public void calculateScore() {
        surveyService.calculateScore(survey);
        state = SurveyState.CALCULATED;
    }

    @ShellMethod(value = "Show summary", key = {"show", "summary", "score"})
    @ShellMethodAvailability(value = "isShowSummaryCommandAvailable")
    public void showSummary() {
        surveyService.showSummary(survey);
        state = SurveyState.SHOWED;
    }

    private Availability isAskQuestionsCommandAvailable() {
        return (survey != null && SurveyState.LOADED.equals(state)) ? Availability.available() : Availability.unavailable("Сначала необходимо загрузить тест");
    }

    private Availability isCalculateScoreCommandAvailable() {
        return (survey != null && SurveyState.EXECUTED.equals(state)) ? Availability.available() : Availability.unavailable("Сначала необходимо выполнить " +
                "тест");
    }

    private Availability isShowSummaryCommandAvailable() {
        return (survey != null && SurveyState.CALCULATED.equals(state)) ? Availability.available() : Availability.unavailable("Сначала необходимо посчтить " +
                "результат");
    }

}

