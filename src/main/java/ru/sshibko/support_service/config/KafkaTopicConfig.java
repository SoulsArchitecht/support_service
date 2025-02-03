package ru.sshibko.support_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {



    @Value("${t1.kafka.topic.transactions-result}")
    private String transactionResultTopicName;



    @Bean
    public NewTopic TransactionResultTopic() {
        return TopicBuilder
                .name(transactionResultTopicName)
                .replicas(2)
                .partitions(3)
                .build();
    }
}
