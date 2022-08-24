package ru.otus.spring.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.List;

@WebMvcTest(ReviewerController.class)
@ContextConfiguration(classes = {ReviewerController.class})
public class ReviewerControllerTest {

    @Autowired
    private MockMvc mvc;

    private static final List<String> reviewList =
            List.of("Кинг уже не тот",
                    "Знакомый говорил, что названия книг ему отдельный человек придумывает",
                    "Это уже было в Симпсонах!",
                    "Кинг дилетант! Я проверял домашки в ОТУСе, вот где ужасы встречаются!",
                    "Ничего не понял, но очень интересно",
                    "Говорят, что он еще и картины пишет",
                    "Зашли и вышли, книга на 20 минут",
                    "Книга огонь! А когда мне заплатят за рецензию?");

    @DisplayName("должен получать рецензию")
    @Test
    void shouldGetRandomString() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/review"))
                .andExpect(content().string(Matchers.in(reviewList)));
    }
}
