package ru.sshibko.support_service.kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class KafkaConsumerProperties {

    @Value("${support_service.kafka.consumer.group-id}")
    private String groupId;

    @Value("${support_service.kafka.bootstrap.server}")
    private String servers;

    @Value("${support_service.kafka.consumer.session-timeout}")
    private String sessionTimeout;

    @Value("${support_service.kafka.consumer.max-partition-fetch-bytes}")
    private String maxPartitionFetchBytes;

    @Value("${support_service.kafka.consumer.max-poll-records:1}")
    private String maxPollRecords;

    @Value("${support_service.kafka.consumer.max-poll-interval:300000}")
    private String maxPollIntervalsMs;

    @Value("${support_service.kafka.consumer.heartbeat-interval}")
    private String heartbeatInterval;

    @Value("${support_service.kafka.consumer.trusted-packages:*}")
    private String trustedPackages;

    @Value("${support_service.kafka.consumer.enable-auto-commit:false}")
    private String enableAutoCommit;

    @Value("${support_service.kafka.consumer.auto-offset-reset:3000}")
    private String autoOffsetReset;

    @Value("${support_service.kafka.consumer.key-serializer}")
    private String keySerializer;

    @Value("${support_service.kafka.consumer.value-serializer}")
    private String valueSerializer;

    @Value("${support_service.kafka.consumer.transaction-group-id}")
    private String transactionGroupId;

    @Value("${support_service.kafka.consumer.account-group-id}")
    private String accountGroupId;

    @Value("${support_service.kafka.consumer.client-group-id}")
    private String clientGroupId;

    @Value("${support_service.kafka.consumer.transaction-accept-group-id}")
    private String transactionAcceptGroupId;

    @Value("${support_service.kafka.consumer.transaction-result-group-id}")
    private String transactionResultGroupId;
}
