package ru.otus.spring.actuator;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.BaseUnits;
import org.springframework.stereotype.Component;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.repositories.PictureRepository;

@Component
public class StephenKingMetricsComponent {

    private final BookRepository bookRepository;
    private final PictureRepository pictureRepository;


    public StephenKingMetricsComponent(BookRepository bookRepository, PictureRepository pictureRepository, MeterRegistry meterRegistry) {
        this.bookRepository = bookRepository;
        this.pictureRepository = pictureRepository;

        Gauge.builder("StephenKing.bookCount", bookRepository::count)
                .baseUnit(BaseUnits.OBJECTS)
                .description("Количество книг на складе Стивена Кинга")
                .register(meterRegistry);

        Gauge.builder("StephenKing.pictureCount", pictureRepository::count)
                .baseUnit(BaseUnits.OBJECTS)
                .description("Количество картин на складе Стивена Кинга")
                .register(meterRegistry);



    }
}
