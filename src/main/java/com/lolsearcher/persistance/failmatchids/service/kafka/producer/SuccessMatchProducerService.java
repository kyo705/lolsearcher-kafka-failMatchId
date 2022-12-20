package com.lolsearcher.persistance.failmatchids.service.kafka.producer;

import com.lolsearcher.persistance.failmatchids.model.entity.match.Match;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaSendCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

@Slf4j
@RequiredArgsConstructor
@Service
public class SuccessMatchProducerService implements producerRecordService<String, Match> {

    @Value("${app.kafka.topics.success_match.name}")
    private String TOPIC_NAME;
    private final KafkaTemplate<String, Match> successMatchTemplate;

    @Transactional(transactionManager = "kafkaTransactionManager")
    public void send(ProducerRecord<String, Match> successMatchRecord) throws ExecutionException, InterruptedException {

        if(!successMatchRecord.topic().equals(TOPIC_NAME)){
            throw new IllegalArgumentException("토픽명이 올바르지 않습니다.");
        }

        KafkaSendCallback<String, Match> callback = successMatchSendCallback();

        ListenableFuture<SendResult<String, Match>> future = successMatchTemplate.send(successMatchRecord);

        future.addCallback(callback);
        future.get();
    }

    @Override
    public ProducerRecord<String, Match> createProducerRecord(Match value){
        return new ProducerRecord<>(TOPIC_NAME, value);
    }

    @Override
    public ProducerRecord<String, Match> createProducerRecord(String key, Match value){
        return new ProducerRecord<>(TOPIC_NAME, key, value);
    }

    @Override
    public ProducerRecord<String, Match> createProducerRecord(int partition, String key, Match value){
        return new ProducerRecord<>(TOPIC_NAME, partition, key, value);
    }


    private KafkaSendCallback<String, Match> successMatchSendCallback(){
        return new KafkaSendCallback<>(){

            @Override
            public void onSuccess(SendResult<String, Match> result) {
                log.info("TOPIC_NAME : '{}'에 MATCH : '{}' 적재 성공",
                        TOPIC_NAME, result.getProducerRecord().value().getMatchId());
            }

            @Override
            public void onFailure(KafkaProducerException ex) {
                log.error("TOPIC_NAME : '{}'에 MATCH : '{}' 적재 실패",
                        TOPIC_NAME, ((Match) ex.getFailedProducerRecord().value()).getMatchId());
            }
        };
    }
}
