package ru.otus.spring;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Picture;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.repositories.PictureRepository;
import ru.otus.spring.service.PublisherService;
import ru.otus.spring.service.ReviewerService;
import java.util.List;

@SpringBootTest
@SpringIntegrationTest
public class StephenKingAssistanceServiceTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    PictureRepository pictureRepository;

    @Mock
    PublisherService publisherService;

    @Mock
    ReviewerService reviewerService;

    private static final int PAGE_COUNT = 450;

    @Test
    public void testStephenKingBookArt() {
        MessageChannel stephenKingArtChannel =
                applicationContext.getBean("assistantFlowChannel",
                        MessageChannel.class);

        Book book = new Book();
        book.setPageCount(PAGE_COUNT);
        stephenKingArtChannel.send(new GenericMessage<>(book));

        List<Book> books = bookRepository.findAll();
        assertEquals(1, books.size());
        assertEquals(PAGE_COUNT, books.get(0).getPageCount());
    }

    @Test
    public void testStephenKingPictureArt() {
        MessageChannel stephenKingArtChannel =
                applicationContext.getBean("assistantFlowChannel",
                        MessageChannel.class);

        stephenKingArtChannel.send(new GenericMessage<>(new Picture()));

        assertEquals(1, pictureRepository.count());
    }
}
