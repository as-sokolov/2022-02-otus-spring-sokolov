package ru.otus.spring.config;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.stereotype.Service;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Picture;
import ru.otus.spring.service.StoringService;
import java.util.Arrays;
import java.util.List;


@Service
public class StephenKingAssistanceIntegrationConfig {

    private static List<String> drawingList = Arrays.asList("Scary", "Funny", "Ugly", "Boring");
    private static List<String> decorationList = Arrays.asList("Rich", "Middle-level", "Paperback");
    private static List<String> frameList = Arrays.asList("Golden", "Redwood", "Silver", "Oak");

    private final StoringService storingService;

    private int artCounter = 0;
    private final int minprice;
    private final int maxprice;
    private final int DEFAULT_MIN_PRICE = 100;

    public StephenKingAssistanceIntegrationConfig(StoringService storingService, @Value("${StephenKing.minprice}") int minprice, @Value("${StephenKing" +
            ".maxprice}") int maxprice) {
        this.storingService = storingService;
        this.minprice = minprice <= 0 ? DEFAULT_MIN_PRICE : minprice;
        this.maxprice = (maxprice <= 0 || maxprice < this.minprice) ? 2 * this.minprice : maxprice;
    }

    @Bean
    public IntegrationFlow assistantFlow() {
        return IntegrationFlows.from("assistantFlowChannel")
                //.log()
                .<Object, Class<?>>route(Object::getClass, m -> m
                        .channelMapping(Picture.class, "pictureFramingChannel")
                        .channelMapping(Book.class, "bookDrawingChannel"))
                .get();
    }

    @Bean
    public IntegrationFlow bookDrawingAddingFlow() {
        return IntegrationFlows.from("bookDrawingChannel")
                //.log()
                .enrich(message -> message.propertyFunction("drawings", m -> drawingList.get(RandomUtils.nextInt(0, drawingList.size()))))
                .channel("bookDecorationChannel")
                .get();
    }

    @Bean
    public IntegrationFlow bookDecorationFlow() {
        return IntegrationFlows.from("bookDecorationChannel")
                //.log()
                .enrich(message -> message.propertyFunction("decoration", m -> decorationList.get(RandomUtils.nextInt(0, decorationList.size()))))
                .channel("namingChannel")
                .get();
    }

    @Bean
    public IntegrationFlow pictureFramingFlow() {
        return IntegrationFlows.from("pictureFramingChannel")
                //.log()
                .enrich(message -> message.propertyFunction("frameMaterial", m -> frameList.get(RandomUtils.nextInt(0, frameList.size()))))
                .channel("namingChannel")
                .get();
    }

    @Bean
    public IntegrationFlow namingFlow() {
        return IntegrationFlows.from("namingChannel")
                //.log()
                .enrich(message -> message.propertyFunction("name", m -> "Art " + ++artCounter))
                .channel("pricingChannel")
                .get();
    }

    @Bean
    public IntegrationFlow pricingFlow() {
        return IntegrationFlows.from("pricingChannel")
                //.log()
                .enrich(message -> message.propertyFunction("price", m -> RandomUtils.nextInt(minprice, maxprice + 1)))
                .channel("storingChannel")
                .get();
    }

    @Bean
    public IntegrationFlow storingFlow() {
        return IntegrationFlows.from("storingChannel")
                .log()
                .handle(storingService, "storeArt")
                .get();
    }
}
