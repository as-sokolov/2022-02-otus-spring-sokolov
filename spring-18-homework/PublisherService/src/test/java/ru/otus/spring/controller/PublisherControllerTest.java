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

@WebMvcTest(PublisherController.class)
@ContextConfiguration(classes = {PublisherController.class})
public class PublisherControllerTest {

    @Autowired
    private MockMvc mvc;

    @DisplayName("должен получать ISBN")
    @Test
    void shouldGetRandomString() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/isbn"))
                .andExpect(content().string(Matchers.matchesRegex("^(?=(?:\\D*\\d){10}(?:(?:\\D*\\d){3})?$)[\\d-]+$")));
    }
}
