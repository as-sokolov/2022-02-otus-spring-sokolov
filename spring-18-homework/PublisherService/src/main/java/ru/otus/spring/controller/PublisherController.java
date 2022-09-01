package ru.otus.spring.controller;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublisherController {

    private int failchance;

    public PublisherController(@Value("${failchance}") int failchance) {
        this.failchance = failchance;
    }

    @GetMapping("/api/isbn")
    public String getISBN() {
        if (RandomUtils.nextInt(1, 101) < failchance) {
            try {
                System.out.println("Start sleeping...." + System.currentTimeMillis());
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("Hystrix thread interupted...." + System.currentTimeMillis());
                e.printStackTrace();
            }
        }
        // ISBN формат xxx-x-xxx-xxxxx-x
        StringBuilder sb = new StringBuilder();
        sb.append(RandomUtils.nextInt(100, 1000)).append("-");
        sb.append(RandomUtils.nextInt(0, 10)).append("-");
        sb.append(RandomUtils.nextInt(100, 1000)).append("-");
        sb.append(RandomUtils.nextInt(10000, 100000)).append("-");
        sb.append(RandomUtils.nextInt(0, 10));
        return sb.toString();
    }
}
