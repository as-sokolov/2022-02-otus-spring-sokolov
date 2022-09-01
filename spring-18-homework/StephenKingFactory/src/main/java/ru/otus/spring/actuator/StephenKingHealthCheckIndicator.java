package ru.otus.spring.actuator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.spring.service.StephenKingHorrorFactoryHealthCheckService;
import java.util.HashMap;
import java.util.Map;

@Component
public class StephenKingHealthCheckIndicator implements HealthIndicator {

    private final StephenKingHorrorFactoryHealthCheckService stephenKingHorrorFactoryHealthCheckService;
    private final int healthCheckThreshold;

    public StephenKingHealthCheckIndicator(StephenKingHorrorFactoryHealthCheckService stephenKingHorrorFactoryHealthCheckService, @Value("${StephenKing" +
            ".healthcheckthreshold}") int healthCheckThreshold) {
        this.stephenKingHorrorFactoryHealthCheckService = stephenKingHorrorFactoryHealthCheckService;
        this.healthCheckThreshold = healthCheckThreshold;
    }

    @Override
    public Health health() {

        Map<String, Object> stephenKingIndicators = new HashMap<>();
        stephenKingIndicators.put("lastActivity", stephenKingHorrorFactoryHealthCheckService.getLastActivity());
        stephenKingIndicators.put("artCreationLastDate", stephenKingHorrorFactoryHealthCheckService.getArtCreationLastDate());

        if (System.currentTimeMillis() - stephenKingHorrorFactoryHealthCheckService.getArtCreationLastDate().getTime() > healthCheckThreshold) {
            stephenKingIndicators.put("noArtInLastSeconds",
                    (System.currentTimeMillis() - stephenKingHorrorFactoryHealthCheckService.getArtCreationLastDate().getTime())/1000);
            return Health.down().withDetails(stephenKingIndicators).build();
        } else {
            return Health.up().withDetails(stephenKingIndicators).build();
        }
    }
}
