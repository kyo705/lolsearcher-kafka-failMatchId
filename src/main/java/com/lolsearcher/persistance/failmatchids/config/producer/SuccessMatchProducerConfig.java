package com.lolsearcher.persistance.failmatchids.config.producer;

import com.lolsearcher.persistance.failmatchids.constant.SuccessMatchProducerConstants;
import com.lolsearcher.persistance.failmatchids.model.entity.match.Match;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.transaction.KafkaTransactionManager;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class SuccessMatchProducerConfig {

    @Bean
    public KafkaTransactionManager<String, Match> kafkaTransactionManager(){
        return new KafkaTransactionManager<>(getProducerFactory());
    }

    @Bean
    public KafkaTemplate<String, Match> successMatchTemplate(){
        return new KafkaTemplate<>(getProducerFactory());
    }

    @Bean
    public DefaultKafkaProducerFactory<String, Match> getProducerFactory(){
        Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, SuccessMatchProducerConstants.BOOTSTRAP_SERVER);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        properties.put(ProducerConfig.ACKS_CONFIG, SuccessMatchProducerConstants.ACK_MODE);
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, SuccessMatchProducerConstants.IDEMPOTENCE);
        properties.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, SuccessMatchProducerConstants.TRANSACTION_ID);
        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, SuccessMatchProducerConstants.COMPRESSION_TYPE);

        return new DefaultKafkaProducerFactory<>(properties);
    }
}
