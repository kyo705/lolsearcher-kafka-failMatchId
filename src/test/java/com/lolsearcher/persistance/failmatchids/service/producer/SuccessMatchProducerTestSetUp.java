package com.lolsearcher.persistance.failmatchids.service.producer;

import com.lolsearcher.persistance.failmatchids.model.entity.match.Match;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

public class SuccessMatchProducerTestSetUp {

    protected static List<Match> consumeSuccessMatches() {

        List<Match> answer = new ArrayList<>();

        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "success_match_group");

        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        properties.put(JsonDeserializer.TRUSTED_PACKAGES, "com.lolsearcher.persistance.failmatchids.model.entity.match");

        properties.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, Match> kafkaConsumer = new KafkaConsumer<>(properties);

        kafkaConsumer.subscribe(List.of("success_match"));

        ConsumerRecords<String, Match> records = kafkaConsumer.poll(Duration.ofSeconds(5));
        for(ConsumerRecord<String, Match> record : records){
            answer.add(record.value());
        }

        return answer;
    }

    protected static Stream<Arguments> successMatchRecords(){

        String TOPIC_NAME = "success_match";

        Match match1 = new Match();
        match1.setMatchId("matchId1");

        Match match2 = new Match();
        match2.setMatchId("matchId2");

        Match match3 = new Match();
        match3.setMatchId("matchId3");

        return Stream.of(
                Arguments.arguments(List.of(
                        new ProducerRecord<String, Match>(TOPIC_NAME, 0, null, match1),
                        new ProducerRecord<String, Match>(TOPIC_NAME,0, null, match2),
                        new ProducerRecord<String, Match>(TOPIC_NAME,0, null, match3)
                ))
        );
    }

}
