package ru.otus.spring.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.spring.integration.StephenKingAssistanceService;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Picture;
import java.util.Date;

@Service
@Slf4j
public class StephenKingHorrorFactoryServiceImpl implements StephenKingHorrorFactoryService, StephenKingHorrorFactoryHealthCheckService {

    public static final String NEW_BOOK_WAS_WRITTEN = "Ура! Стивен Кинг написал новую книгу и готов к следующей";
    public static final String NEW_PICTURE_WAS_WRITTEN = "Ура! Стивен Кинг жив-здоров и написал новую картину!";
    private final int minpagerate;
    private final int maxpagerate;
    private final int picturechance;
    private final int bookpagecount;

    private static final int DEFAULT_BOOK_PAGE_COUNT = 250;

    private final StephenKingAssistanceService service;

    // Метрики для Стивена Кинга
    private Date artCreationLastDate = new Date();
    private String lastActivity = "Стивен Кинг проснуля, выпил кофе и сел призадумавшись";

    public StephenKingHorrorFactoryServiceImpl(@Value("${StephenKing.minpagerate}") int minpagerate, @Value("${StephenKing.maxpagerate}") int maxpagerate, @Value("$" +
            "{StephenKing.picturechance}") int picturechance,@Value("${StephenKing.bookpagecount}") int bookpagecount, StephenKingAssistanceService service) {
        this.minpagerate = minpagerate < 0 ? 0 : minpagerate;
        this.maxpagerate = (maxpagerate <= 0 || maxpagerate < this.minpagerate) ? this.minpagerate + 1 : maxpagerate;
        this.picturechance = picturechance;
        this.bookpagecount = bookpagecount <=0 ? DEFAULT_BOOK_PAGE_COUNT : bookpagecount;
        this.service = service;
    }


    @Override
    public void letsMagicBegin() throws InterruptedException {
        int writedPages = 0;
        while ( true ) {
            Thread.sleep( 500 );

            if (RandomUtils.nextInt(0, 101) <= picturechance) {
                artCreationLastDate = new Date();
                log.info("Stephen King painted new Picture!");
                lastActivity= NEW_PICTURE_WAS_WRITTEN;
                service.doDirtyJob(new Picture());
            } else {
                int freshWritedPages = RandomUtils.nextInt(minpagerate, maxpagerate + 1);
                log.info("Stephen King writed some new pages. Count = " + freshWritedPages);
                writedPages += freshWritedPages;
                if (writedPages > bookpagecount) {
                    Book book = new Book();
                    book.setPageCount(writedPages);
                    lastActivity = NEW_BOOK_WAS_WRITTEN;
                    artCreationLastDate = new Date();
                    service.doDirtyJob(book);
                    writedPages = 0;
                }
            }
        }

    }

    @Override
    public Date getArtCreationLastDate() {
        return artCreationLastDate;
    }

    @Override
    public String getLastActivity() {
        return lastActivity;
    }
}
