package ru.otus.spring.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import ru.otus.spring.feign.ReviewerServiceProxy;

@Service
public class ReviewerServiceImpl implements ReviewerService {

    private final ReviewerServiceProxy reviewerService;

    public ReviewerServiceImpl(ReviewerServiceProxy reviewerService) {
        this.reviewerService = reviewerService;
    }

    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "500")
    }, fallbackMethod = "getReviewFallback")
    public String getReview() {
        return reviewerService.getReview();
    }

    private String getReviewFallback() {
        return "Нет рецензии";
    }
}
