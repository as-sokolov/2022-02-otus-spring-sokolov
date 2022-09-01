package ru.otus.spring.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import ru.otus.spring.feign.PublisherServiceProxy;

@Service
public class PublisherServiceImpl implements PublisherService {

    private final PublisherServiceProxy publisherService;

    public PublisherServiceImpl(PublisherServiceProxy publisherService) {
        this.publisherService = publisherService;
    }


    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "500")
    }, fallbackMethod = "getIsbnNumberFallback")
    @Override
    public String getISBN() {
        return publisherService.getISBN();
    }

    private String getIsbnNumberFallback() {
        return "Не присвоен";
    }


}
