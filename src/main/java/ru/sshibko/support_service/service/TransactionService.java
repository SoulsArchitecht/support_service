package ru.sshibko.support_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import ru.sshibko.support_service.enums.TransactionStatus;
import ru.sshibko.support_service.exception.SendMessageException;
import ru.t1.dto.TransactionAcceptDto;
import ru.t1.dto.TransactionResultDto;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {

    private final KafkaTemplate<String, Object> template;

    private final String MESSAGE_KEY = String.valueOf(UUID.randomUUID());

    @Value("${support_service.kafka.topic.transactions-result}")
    private String transactionResultTopicName;

    private final TransactionLimiter transactionLimiter;

    /** Sending message to Kafka
     *
     * @param topic - String topicName
     * @param object - T dtoObject
     */
    public void sendTransactionResultMessage(String topic, Object object) {
        Map<String, Object> headers = new HashMap<>();
        headers.put(KafkaHeaders.TOPIC, topic);
        headers.put(KafkaHeaders.KEY, MESSAGE_KEY);
        Message<Object> messageWithHeaders = MessageBuilder
                .withPayload(object)
                .copyHeaders(headers)
                .build();
        try {
            template.send(messageWithHeaders);
        } catch (SendMessageException ex) {
            log.error("Error sending transactionResult message", ex);
        } finally {
            template.flush();
        }
    }

    public void processTransactionResult(TransactionAcceptDto transactionAcceptDto) {

        TransactionStatus status;

        if (transactionLimiter.isThresholdExceedance(transactionAcceptDto.getAccountId(),
                transactionAcceptDto.getCreatedAt())) {
            status = TransactionStatus.BLOCKED;
        } else if (isInsufficientFunds(transactionAcceptDto)) {
            log.warn("There are insufficient funds in the account with accountId " +
                    transactionAcceptDto.getAccountId());
            status = TransactionStatus.REJECTED;
        } else {
            status = TransactionStatus.ACCEPTED;
        }

        TransactionResultDto transactionResultDto = TransactionResultDto.builder()
                .accountId(transactionAcceptDto.getAccountId())
                .transactionId(transactionAcceptDto.getTransactionId())
                .transactionStatus(String.valueOf(status))
                .build();

        log.info("Transaction with ID : {} processed successfully", transactionAcceptDto.getTransactionId());
        sendTransactionResultMessage(transactionResultTopicName, transactionResultDto);
    }

    private boolean isInsufficientFunds(TransactionAcceptDto transactionAcceptDto) {
        return transactionAcceptDto.getAccountBalance().compareTo(
                transactionAcceptDto.getTransactionAmount()) < 0;
    }

}
