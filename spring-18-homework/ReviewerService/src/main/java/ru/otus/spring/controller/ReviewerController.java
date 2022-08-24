package ru.otus.spring.controller;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class ReviewerController {

    private final int failchance;

    // Список рецензий на книгу
    private static final List<String> reviewList =
            List.of("Кинг уже не тот",
                    "Знакомый говорил, что названия книг ему отдельный человек придумывает",
                    "Это уже было в Симпсонах!",
                    "Кинг дилетант! Я проверял домашки в ОТУСе, вот где ужасы встречаются!",
                    "Ничего не понял, но очень интересно",
                    "Говорят, что он еще и картины пишет",
                    "Зашли и вышли, книга на 20 минут",
                    "Книга огонь! А когда мне заплатят за рецензию?");

    public ReviewerController(@Value("${failchance}") int failchance) {
        this.failchance = failchance;
    }

    @GetMapping("/api/review")
    public String getReview() {
        if (RandomUtils.nextInt(1, 101) < failchance) {
            try {
                System.out.println("Start sleeping...." + System.currentTimeMillis());
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("Hystrix thread interupted...." + System.currentTimeMillis());
                e.printStackTrace();
            }
        }
        // Возвращается случайная рецензия из списка
        return reviewList.get(RandomUtils.nextInt(0, reviewList.size()));
    }
}
