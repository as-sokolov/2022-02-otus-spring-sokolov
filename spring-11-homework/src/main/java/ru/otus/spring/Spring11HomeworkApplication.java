package ru.otus.spring;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongock
@EnableMongoRepositories
public class Spring11HomeworkApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Spring08HomeworkApplication.class, args);
    }
}
