package com.lolsearcher.persistance.failmatchids.config;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.kafka.common.IsolationLevel;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
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
public class FailMatchKafkaConsumerConfig {
    @Value("${app.kafka.zookeeper.cluster.lolsearcher.brokers.broker1.server}")
    private String BOOTSTRAP_SERVER;
    private static final Integer CONCURRENCY_COUNT = Runtime.getRuntime().availableProcessors();
    private static final Integer POLL_RECORDS_COUNT = 100; /* riot api 요청 제한 횟수가 2분당 최대 100회 */
    private static final Integer HEARTBEAT_MS = 2000;
    private static final Integer SESSION_TIME_OUT_MS = 2*60*1000 + 2000; /* 2min + heartbeat time */

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> failMatchIdsContainerFactory(){
        //컨슈머 팩토리 생성
        Map<String, Object> properties = createConsumerProperties();
        DefaultKafkaConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(properties);

        //컨슈머 리스너 팩토리 생성
        ConcurrentKafkaListenerContainerFactory<String, String> listenerFactory = new ConcurrentKafkaListenerContainerFactory<>();

        listenerFactory.setConsumerFactory(consumerFactory);
        listenerFactory.setConcurrency(CONCURRENCY_COUNT);
        listenerFactory.setBatchListener(true);

        listenerFactory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        listenerFactory.getContainerProperties().setConsumerRebalanceListener(createRebalanceListener());

        return listenerFactory;
    }

    private Map<String, Object> createConsumerProperties() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, POLL_RECORDS_COUNT);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, OffsetResetStrategy.NONE);
        properties.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, IsolationLevel.READ_COMMITTED);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        properties.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, HEARTBEAT_MS);
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, SESSION_TIME_OUT_MS);

        return properties;
    }

    private ConsumerAwareRebalanceListener createRebalanceListener(){
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
                //해당 컨슈머 그룹은 반드시 파티션과 컨슈머가 1:1 매핑되도록 설계
                if(partitions.size() != 1){
                    throw new IncorrectResultSizeDataAccessException(1);
                }
            }
        };
    }
}
