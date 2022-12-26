package com.lolsearcher.persistance.failmatchids.config.topic;

import com.lolsearcher.persistance.failmatchids.constant.FailMatchIdTopicConstants;
import com.lolsearcher.persistance.failmatchids.constant.SuccessMatchTopicConstants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TopicConfig {

    @Bean
    public NewTopic failMatchIdsTopic(){
        return TopicBuilder.name(FailMatchIdTopicConstants.TOPIC_NAME)
                .replicas(FailMatchIdTopicConstants.REPLICA_COUNT)
                .partitions(FailMatchIdTopicConstants.PARTITION_COUNT)
                .build();
    }

    @Bean
    public NewTopic successMatchesTopic(){
        return TopicBuilder.name(SuccessMatchTopicConstants.TOPIC_NAME)
                .replicas(SuccessMatchTopicConstants.REPLICA_COUNT)
                .partitions(SuccessMatchTopicConstants.PARTITION_COUNT)
                .build();
    }
}
