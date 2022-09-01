package ru.otus.spring.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "publisher-service")
public interface PublisherServiceProxy {
    @GetMapping("/api/isbn")
    public String getISBN();
}