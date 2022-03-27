package ru.otus.homework;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.homework.service.SurveyService;

@Slf4j
public class Main {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
        SurveyService service = context.getBean(SurveyService.class);
        service.executeSurvey();
    }

}
