package ru.otus.spring;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.models.Book;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.repositories.PictureRepository;

@SpringBootTest
@SpringIntegrationTest
@AutoConfigureMockMvc
public class StephenKingHealthIndicatorComponentTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    PictureRepository pictureRepository;

    @Autowired
    private MockMvc mvc;

    private static final int PAGE_COUNT = 450;

    @Autowired
    private ObjectMapper mapper;


    @Test
    public void testStephenKingMetric() throws Exception {
        MessageChannel stephenKingArtChannel =
                applicationContext.getBean("assistantFlowChannel",
                        MessageChannel.class);
        Book book = new Book();
        book.setPageCount(PAGE_COUNT);
        stephenKingArtChannel.send(new GenericMessage<>(book));

        mvc.perform(get("/actuator/metrics/StephenKing.bookCount"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("1.0")));
    }

}