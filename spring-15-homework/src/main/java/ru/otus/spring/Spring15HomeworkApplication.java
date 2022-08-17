package ru.otus.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import ru.otus.spring.service.StephenKingHorrorFactoryService;

@IntegrationComponentScan
@EnableIntegration
@SpringBootApplication
public class Spring15HomeworkApplication {
    public static void main(String[] args) throws Exception {
        ApplicationContext context = SpringApplication.run(Spring15HomeworkApplication.class, args);
        StephenKingHorrorFactoryService service = context.getBean(StephenKingHorrorFactoryService.class);
        service.letsMagicBegin();
    }
}
