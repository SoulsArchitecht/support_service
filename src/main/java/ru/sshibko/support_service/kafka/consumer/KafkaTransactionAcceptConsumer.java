package ru.sshibko.support_service.kafka.consumer;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.sshibko.support_service.exception.ProcessingException;
import ru.sshibko.support_service.service.TransactionService;
import ru.t1.dto.TransactionAcceptDto;

import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaTransactionAcceptConsumer {

    private final TransactionService transactionService;

    @KafkaListener(topics = "${support_service.kafka.topic.transactions-accept}",
    groupId = "${support_service.kafka.consumer.transaction-group-id}",
    containerFactory = "kafkaListenerContainerFactoryTransactionAccept")
    public void transactionAcceptListener(@Payload List<TransactionAcceptDto> messages,
                                          Acknowledgment ack,
                                          @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                          @Header(KafkaHeaders.RECEIVED_KEY) String key
    ) {
        log.info("Topic: {}, Key: {} start proceeding", topic, key);
        try {
            messages.forEach(transactionService::processTransactionResult);
        } catch (ProcessingException e) {
            log.error("Error processing transaction", e);
        } finally {
            ack.acknowledge();
        }
        log.info("Topic: {}, Key: {} end proceeding", topic, key);
    }
}
