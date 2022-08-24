package ru.otus.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.web.client.RestTemplate;
import ru.otus.spring.service.StephenKingHorrorFactoryService;

@IntegrationComponentScan
@EnableIntegration
@SpringBootApplication
@EnableFeignClients(basePackages = "ru.otus.spring.feign")
@EnableHystrix
public class Spring18HomeworkApplication {
    public static void main(String[] args) throws Exception {
        ApplicationContext context = SpringApplication.run(Spring18HomeworkApplication.class, args);
        StephenKingHorrorFactoryService service = context.getBean(StephenKingHorrorFactoryService.class);
        service.letsMagicBegin();
    }
}
