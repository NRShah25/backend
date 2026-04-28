package com.financetracker.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertService {

    private final SqsClient sqsClient;

    @Value("${aws.sqs.queue-url}")
    private String queueUrl;

    public void sendBudgetAlert(String email, String category,
            String status, double percentageUsed) {

        String message = String.format(
            "{\"email\":\"%s\",\"category\":\"%s\"," +
            "\"status\":\"%s\",\"percentageUsed\":%.1f}",
            email, category, status, percentageUsed);

        try {
            SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(message)
                    .build();

            sqsClient.sendMessage(request);
            log.info("Budget alert sent to SQS for user: {} category: {}",
                    email, category);

        } catch (Exception e) {
            log.warn("SQS not available, logging alert locally: {}", message);
        }
    }
}