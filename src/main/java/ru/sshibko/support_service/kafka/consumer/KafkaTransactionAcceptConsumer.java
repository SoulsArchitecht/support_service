package ru.sshibko.support_service.kafka.consumer;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.sshibko.support_service.dto.TransactionAcceptDto;
import ru.sshibko.support_service.dto.TransactionResultDto;
import ru.sshibko.support_service.service.TransactionService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaTransactionAcceptConsumer {

    private final TransactionService transactionService;

    @KafkaListener(topics = "${support_service.kafka.topic-transactions-accept}",
    groupId = "${support_service.kafka.consumer.transaction-group-id}",
    containerFactory = "kafkaListenerContainerFactoryTransactionAccept")
    public void transactionAcceptListener(@Payload List<TransactionAcceptDto> messages,
                                          Acknowledgment ack,
                                          @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                          @Header(KafkaHeaders.RECEIVED_KEY) String key
    ) {
        log.debug("some");
        try {
            messages.forEach(dto -> {
                TransactionAcceptDto transactionAcceptDto = TransactionAcceptDto.builder()
                        .clientId(dto.getClientId())
                        .accountId(dto.getAccountId())
                        .transactionId(dto.getTransactionId())
                        .createdAt(dto.getCreatedAt())
                        .transactionAmount(dto.getTransactionAmount())
                        .accountBalance(dto.getAccountBalance())
                        .build();
                TransactionResultDto transactionResultDto = TransactionResultDto.builder()
                        .accountId(dto.getAccountId())
                                .transactionId(dto.getTransactionId())
                                        .transactionStatus("NONE")
                                                .build();
                //TODO logic for status filtering
                //if (dto.getCreatedAt())

/*                if (dto.getAccountBalance().longValue() < dto.getTransactionAmount().longValue()) {
                    sendMessage(status Rejected);
                }*/

                //sendMessage (status Accepted)
                //transactionService.sendTransactionAcceptMessage(transactionResultDto);
            });

        } finally {
            ack.acknowledge();
        }
    }


    //TODO finish method
    public List<TransactionAcceptDto> processTransactions(List<TransactionAcceptDto> transactions, Integer N, Duration T) {
        Map<String, List<TransactionAcceptDto>> groupedTransactions = transactions.stream()
                .collect(Collectors.groupingBy(TransactionAcceptDto::getAccountId));

        for (Map.Entry<String, List<TransactionAcceptDto>> entry : groupedTransactions.entrySet()) {
            String accountId = entry.getKey();
            List<TransactionAcceptDto> accountTransactions = entry.getValue();

            // Sorting transactions by timestamp
            accountTransactions.sort(Comparator.comparing(TransactionAcceptDto::getCreatedAt));

            for (int i = 0; i <= accountTransactions.size() - N; i++) {
                List<TransactionAcceptDto> slice = accountTransactions.subList(i, i + N);
                LocalDateTime firstCreatedAt = slice.get(0).getCreatedAt();
                LocalDateTime lastCreatedAt = slice.get(N - 1).getCreatedAt();

                // Check that all transaction were in time interval T
                if (Duration.between(firstCreatedAt, lastCreatedAt).compareTo(T) <= 0) {
                    //slice.forEach(transaction -> transaction.setStatus("BLOCKED"));
                }
            }
        }

        return transactions;
    }
}
