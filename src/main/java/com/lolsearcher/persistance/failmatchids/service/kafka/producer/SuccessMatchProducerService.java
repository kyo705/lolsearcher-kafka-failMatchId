package com.lolsearcher.persistance.failmatchids.service.kafka.producer;

import com.lolsearcher.persistance.failmatchids.constant.SuccessMatchTopicConstants;
import com.lolsearcher.persistance.failmatchids.model.entity.match.Match;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaSendCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

@Slf4j
@RequiredArgsConstructor
@Service
public class SuccessMatchProducerService implements producerRecordService<String, Match> {

    private final KafkaTemplate<String, Match> successMatchTemplate;

    public void send(ProducerRecord<String, Match> successMatchRecord) throws ExecutionException, InterruptedException {

        if(!successMatchRecord.topic().equals(SuccessMatchTopicConstants.TOPIC_NAME)){
            throw new IllegalArgumentException("토픽명이 올바르지 않습니다.");
        }

        ListenableFuture<SendResult<String, Match>> future = successMatchTemplate.send(successMatchRecord);

        future.addCallback(successMatchSendCallback());
        future.get();
    }

    @Override
    public ProducerRecord<String, Match> createProducerRecord(Match value){
        return new ProducerRecord<>(SuccessMatchTopicConstants.TOPIC_NAME, value);
    }

    @Override
    public ProducerRecord<String, Match> createProducerRecord(String key, Match value){
        return new ProducerRecord<>(SuccessMatchTopicConstants.TOPIC_NAME, key, value);
    }

    @Override
    public ProducerRecord<String, Match> createProducerRecord(int partition, String key, Match value){
        return new ProducerRecord<>(SuccessMatchTopicConstants.TOPIC_NAME, partition, key, value);
    }


    private KafkaSendCallback<String, Match> successMatchSendCallback(){
        return new KafkaSendCallback<>(){

            @Override
            public void onSuccess(SendResult<String, Match> result) {
                log.info("TOPIC_NAME : '{}'에 MATCH : '{}' 적재 성공",
                        SuccessMatchTopicConstants.TOPIC_NAME,
                        result.getProducerRecord().value().getMatchId());
            }

            @Override
            public void onFailure(KafkaProducerException ex) {
                log.error("TOPIC_NAME : '{}'에 MATCH : '{}' 적재 실패",
                        SuccessMatchTopicConstants.TOPIC_NAME,
                        ((Match) ex.getFailedProducerRecord().value()).getMatchId());
            }
        };
    }
}
