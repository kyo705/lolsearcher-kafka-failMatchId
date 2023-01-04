package com.lolsearcher.persistance.failmatchids.config.consumer;

import com.lolsearcher.persistance.failmatchids.constant.FailMatchIdConsumerConstants;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ConsumerAwareRebalanceListener;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Configuration
public class FailMatchIdConsumerConfig {

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> failMatchIdsContainerFactory(){

        ConcurrentKafkaListenerContainerFactory<String, String> listenerFactory = new ConcurrentKafkaListenerContainerFactory<>();

        listenerFactory.setConsumerFactory(failMatchIdsConsumerFactory());
        listenerFactory.setConcurrency(Runtime.getRuntime().availableProcessors());
        listenerFactory.setBatchListener(false); //컨슈머 레코드 하나씩 처리하는 전략 선택 => 중요한 데이터이기 때문

        listenerFactory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        listenerFactory.getContainerProperties().setConsumerRebalanceListener(createRebalanceListener());

        return listenerFactory;
    }

    private DefaultKafkaConsumerFactory<String, String> failMatchIdsConsumerFactory() {

        Map<String, Object> properties = new HashMap<>();

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, FailMatchIdConsumerConstants.BOOTSTRAP_SERVER);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, FailMatchIdConsumerConstants.POLL_RECORDS_COUNT);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, FailMatchIdConsumerConstants.AUTO_OFFSET_COMMIT);
        properties.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, FailMatchIdConsumerConstants.HEARTBEAT_MS);
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, FailMatchIdConsumerConstants.SESSION_TIME_OUT_MS);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, FailMatchIdConsumerConstants.AUTO_OFFSET_RESET);
        properties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, FailMatchIdConsumerConstants.POLL_MS);

        return new DefaultKafkaConsumerFactory<>(properties);
    }


    private ConsumerAwareRebalanceListener createRebalanceListener(){
        //noinspection NullableProblems
        return new ConsumerAwareRebalanceListener() {
            @Override
            public void onPartitionsRevokedBeforeCommit(Consumer<?, ?> consumer, Collection<TopicPartition> partitions) {
                //nothing to do -> 개별 레코드를 하나하나 커밋했기 때문
            }

            @Override
            public void onPartitionsRevokedAfterCommit(Consumer<?, ?> consumer, Collection<TopicPartition> partitions) {
                //nothing to do
            }

            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                //nothing to do
            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                //새롭게 할당받은 파티션
            }
        };
    }
}
