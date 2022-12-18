package com.lolsearcher.persistance.failmatchids.service.consumer;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.transaction.TransactionStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FailMatchIdConsumerTestSetUp {
    private static final String TOPIC_NAME = "filtered_fail_match_id";
    protected static List<ProducerRecord<String, String>> getFailMatchIdRecord() {


        return List.of(
                new ProducerRecord<>(TOPIC_NAME, 0, null, "matchId1"),
                new ProducerRecord<>(TOPIC_NAME, 2, null,"matchId2"),
                new ProducerRecord<>(TOPIC_NAME, 1, null,"matchId3")
        );
    }


    protected static void sendProducerRecords(List<ProducerRecord<String, String>> records) {
        Map<String, Object> properties = createProperties();

        ProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory<>(properties);

        KafkaTransactionManager<String, String> transactionManager = new KafkaTransactionManager<>(producerFactory);

        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(producerFactory);


        //트랜잭션 시작
        TransactionStatus transactionStatus = transactionManager.getTransaction(null);
        try{
            sendRecords(kafkaTemplate, records);

            //트랜잭션 종료
            transactionStatus.flush();
            transactionManager.commit(transactionStatus);
        }catch (Exception e){
            transactionManager.rollback(transactionStatus);
        }
    }

    private static void sendRecords(KafkaTemplate<String, String> kafkaTemplate, List<ProducerRecord<String, String>> records) {
        for(ProducerRecord<String, String> record : records){
            kafkaTemplate.send(record);
        }
    }


    private static Map<String, Object> createProperties(){
        Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        properties.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "fail_match_ids");

        return properties;
    }
}
