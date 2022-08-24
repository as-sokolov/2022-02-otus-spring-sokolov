package ru.otus.spring.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "reviewer-service")
public interface ReviewerServiceProxy {

    @GetMapping("/api/review")
    public String getReview();
}