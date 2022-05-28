package ru.otus.homework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.otus.homework.service.SurveyService;

@SpringBootApplication
public class Spring03HomeworkApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(Spring03HomeworkApplication.class, args);
		SurveyService service = context.getBean(SurveyService.class);
		service.executeSurvey();
	}

}
