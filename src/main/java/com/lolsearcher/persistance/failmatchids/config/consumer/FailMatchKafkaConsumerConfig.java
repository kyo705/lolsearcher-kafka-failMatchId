package com.lolsearcher.persistance.failmatchids.config.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
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
public class FailMatchKafkaConsumerConfig {

    //listenerContainerFactory 설정
    private int CONCURRENCY_COUNT = Runtime.getRuntime().availableProcessors();

    //ConsumerFactory 설정
    @Value("${app.kafka.zookeeper.clusters.lolsearcher.brokers.broker1.server}")
    private String BOOTSTRAP_SERVER;
    @Value("${app.kafka.consumers.filtered_fail_match.poll_record_size}")
    private int POLL_RECORDS_COUNT; /* riot api 요청 제한 횟수가 2분당 최대 100회 */
    @Value("${app.kafka.consumers.filtered_fail_match.heartbeat}")
    private int HEARTBEAT_MS;
    @Value("${app.kafka.consumers.filtered_fail_match.session_time_out}")
    private int SESSION_TIME_OUT_MS; /* 2min + heartbeat time */
    @Value("${app.kafka.consumers.filtered_fail_match.auto_offset_reset}")
    private String AUTO_OFFSET_RESET;
    @Value("${app.kafka.consumers.filtered_fail_match.isolation}")
    private String ISOLATION_LEVEL;
    @Value("${app.kafka.consumers.filtered_fail_match.enable_auto_commit}")
    private boolean AUTO_OFFSET_COMMIT;


    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> failMatchIdsContainerFactory(){

        ConcurrentKafkaListenerContainerFactory<String, String> listenerFactory = new ConcurrentKafkaListenerContainerFactory<>();

        listenerFactory.setConsumerFactory(failMatchIdsConsumerFactory());
        listenerFactory.setConcurrency(CONCURRENCY_COUNT);
        listenerFactory.setBatchListener(false); //컨슈머 레코드 하나씩 처리하는 전략 선택 => 중요한 데이터이기 때문

        listenerFactory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        listenerFactory.getContainerProperties().setConsumerRebalanceListener(createRebalanceListener());

        return listenerFactory;
    }

    private DefaultKafkaConsumerFactory<String, String> failMatchIdsConsumerFactory() {

        Map<String, Object> properties = new HashMap<>();

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, POLL_RECORDS_COUNT);
        properties.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, ISOLATION_LEVEL);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, AUTO_OFFSET_COMMIT);
        properties.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, HEARTBEAT_MS);
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, SESSION_TIME_OUT_MS);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, AUTO_OFFSET_RESET);

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
