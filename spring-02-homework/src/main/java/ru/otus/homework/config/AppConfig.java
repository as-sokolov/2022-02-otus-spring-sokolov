package ru.otus.homework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
@PropertySource("classpath:application.properties")
public class AppConfig {
    @Bean("SurveyCSV")
    public Resource getSurvey(@Value("${survey.filename}") String source) {
        return new ClassPathResource(source);
    }
}