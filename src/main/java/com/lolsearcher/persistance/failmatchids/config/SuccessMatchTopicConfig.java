package com.lolsearcher.persistance.failmatchids.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class SuccessMatchTopicConfig {

    @Value("${app.kafka.topics.success_match.name}")
    private String TOPIC_NAME;
    @Value("${app.kafka.topics.success_match.replica}")
    private int REPLICA_COUNT;
    @Value("${app.kafka.topics.success_match.partitions}")
    private int PARTITION_COUNT;

    @Bean
    public NewTopic successMatchesTopic(){
        return TopicBuilder.name(TOPIC_NAME)
                .replicas(REPLICA_COUNT)
                .partitions(PARTITION_COUNT)
                .build();
    }
}
