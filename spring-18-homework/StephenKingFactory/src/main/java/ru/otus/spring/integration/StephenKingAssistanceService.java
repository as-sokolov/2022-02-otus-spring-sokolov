package ru.otus.spring.integration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.spring.models.StephenKingArt;

@MessagingGateway
public interface StephenKingAssistanceService {
    @Gateway(requestChannel = "assistantFlowChannel")
    void doDirtyJob(StephenKingArt art);
}
