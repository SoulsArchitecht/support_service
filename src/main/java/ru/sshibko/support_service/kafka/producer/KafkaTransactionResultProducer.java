package ru.sshibko.support_service.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.sshibko.support_service.dto.TransactionResultDto;
import ru.sshibko.support_service.service.TransactionService;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaTransactionResultProducer {

    @Value("${support_service.kafka.topic.transactions-result}")
    private String transactionResultTopicName;

    private final TransactionService transactionService;

    public void sendTransactionResult(TransactionResultDto transactionResultDto) {
        transactionService.sendTransactionResultMessage(transactionResultTopicName, transactionResultDto);
    }

}
