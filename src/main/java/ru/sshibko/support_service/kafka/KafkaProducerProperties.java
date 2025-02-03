package ru.sshibko.support_service.kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class KafkaProducerProperties {

    @Value("${support_service.kafka.bootstrap.server}")
    private String servers;

    @Value("${support_service.kafka.producer.retries}")
    private String retries;

    @Value("${support_service.kafka.producer.retry-backoff-ms}")
    private String retryBackoffMs;

    @Value("${support_service.kafka.producer.enable-idempotence}")
    private boolean enableIdempotence;

    @Value("${support_service.kafka.producer.key-serializer}")
    private String keySerializer;

    @Value("${support_service.kafka.producer.value-serializer}")
    private String valueSerializer;

    @Value("${support_service.kafka.producer.acks}")
    private String acks;
}
