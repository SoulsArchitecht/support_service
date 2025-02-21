package ru.sshibko.support_service.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;

/** You may to switch to alternate Transaction Limiter with Redis,
 * but you'll need to realize some additional settings
 * This service build does not include redis config, redis props and redis dependencies
 * You need docker-compose with Redis setting still.
 * Александр, класс RedisTransactionLimiter оставлен в качестве альтернативного примера. Все пояснения выше.
 */
@Component
public class TransactionLimiter {
    private Cache<String, LinkedList<LocalDateTime>> transactionsCache;

    @Value("${support_service.kafka.consumer.transaction-threshold}")
    private int threshold;

    @Value("${support_service.kafka.consumer.transaction-time-frame}")
    private int timeFrame;

    @PostConstruct
    private void configureTransactionsCache() {
        transactionsCache = CacheBuilder.newBuilder()
                .expireAfterAccess(Duration.of(timeFrame, ChronoUnit.SECONDS))
                .build();
    }

    @SneakyThrows
    public boolean isThresholdExceedance(String accountId, LocalDateTime createdAt) {
        var  transactionsTimeList = transactionsCache.get(accountId, LinkedList::new);

        if (isMaximumTransactionCount(transactionsTimeList) &&
                isTransactionIntervalExceeded(transactionsTimeList)) {
            return true;
        }

        if (isMaximumTransactionCount(transactionsTimeList)) {
            transactionsTimeList.set(0, transactionsTimeList.get(1));
            transactionsTimeList.set(threshold - 1, createdAt);
        } else {
            transactionsTimeList.add(createdAt);
        }

        return false;
    }

    private boolean isMaximumTransactionCount(LinkedList<LocalDateTime> transactionTimeList) {
        return transactionTimeList.size() == threshold;
    }

    private boolean isTransactionIntervalExceeded(LinkedList<LocalDateTime> transactionTimeList) {
        return transactionTimeList.getFirst()
                .plus(timeFrame, ChronoUnit.SECONDS)
                .isAfter(transactionTimeList.getLast());
    }
}
