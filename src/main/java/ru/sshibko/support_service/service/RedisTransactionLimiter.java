package ru.sshibko.support_service.service;

/*import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;*/


/**Transaction Limiter by Redis
 * but you'll need to realize some additional settings
 * This service build does not include redis config, redis props and redis dependencies
 * You need docker-compose with Redis setting still.
 */
/*
@Component
@RequiredArgsConstructor
public class RedisTransactionLimiter {

    private final RedisTemplate<String, LocalDateTime> redisTemplate;

    private final ListOperations<String, LocalDateTime> listOperations;

    @Value("${support_service.kafka.consumer.transaction-threshold}")
    private int threshold;

    @Value("${support_service.kafka.consumer.transaction-time-frame}")
    private int timeFrame;

    @SneakyThrows
    public boolean isThresholdExceedance(String accountId, LocalDateTime createdAt) {
        List<LocalDateTime> transactionsTimeList = listOperations.range(accountId, 0, -1);

        if (transactionsTimeList == null) {
            transactionsTimeList = List.of();
        }

        if (isMaximumTransactionCount(transactionsTimeList) &&
                isTransactionIntervalExceeded(transactionsTimeList)) {
            return true;
        }

        if (isMaximumTransactionCount(transactionsTimeList)) {
            listOperations.set(accountId, 0, transactionsTimeList.get(1));
            listOperations.set(accountId, threshold -1, createdAt);
        } else {
            listOperations.rightPush(accountId, createdAt);
        }

        redisTemplate.expire(accountId, timeFrame, TimeUnit.of(ChronoUnit.SECONDS));

        return false;
    }

    private boolean isMaximumTransactionCount(List<LocalDateTime> transactionTimeList) {
        return transactionTimeList.size() == threshold;
    }

    private boolean isTransactionIntervalExceeded(List<LocalDateTime> transactionTimeList) {
        if (transactionTimeList.isEmpty() || transactionTimeList.size() == 1) return false;
        return transactionTimeList.get(0)
                .plus(timeFrame, ChronoUnit.SECONDS)
                .isAfter(transactionTimeList.get(transactionTimeList.size()-1));
    }
}
*/
