package com.lolsearcher.persistance.failmatchids.config;

import com.lolsearcher.persistance.failmatchids.model.entity.match.Match;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.transaction.KafkaTransactionManager;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class SuccessMatchKafkaProducerConfig {

    @Value("${app.kafka.zookeeper.clusters.lolsearcher.brokers.broker1.server}")
    private String BOOTSTRAP_SERVER;
    @Value("${app.kafka.producers.success_match.ack}")
    private String ACK_MODE;
    @Value("${app.kafka.producers.success_match.transaction_id}")
    private String TRANSACTION_ID;

    @Bean
    public KafkaTransactionManager<String, Match> kafkaTransactionManager(){
        return new KafkaTransactionManager<>(getProducerFactory());
    }

    @Bean
    public KafkaTemplate<String, Match> successMatchTemplate(){
        ProducerFactory<String, Match> producerFactory = getProducerFactory();

        return new KafkaTemplate<>(producerFactory);
    }

    private ProducerFactory<String, Match> getProducerFactory(){
        Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        properties.put(ProducerConfig.ACKS_CONFIG, ACK_MODE);
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        properties.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, TRANSACTION_ID);

        return new DefaultKafkaProducerFactory<>(properties);
    }
}
