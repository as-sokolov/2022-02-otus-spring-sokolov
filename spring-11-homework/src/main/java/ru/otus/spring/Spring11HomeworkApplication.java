package ru.otus.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import ru.otus.spring.dbinit.Initializer;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class Spring11HomeworkApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Spring11HomeworkApplication.class, args);
        context.getBean(Initializer.class).init();
    }
}
