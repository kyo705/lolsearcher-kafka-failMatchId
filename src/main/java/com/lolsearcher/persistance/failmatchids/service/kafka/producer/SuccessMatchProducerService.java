package com.lolsearcher.persistance.failmatchids.service.kafka.producer;

import com.lolsearcher.persistance.failmatchids.model.entity.match.Match;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaSendCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;

@Slf4j
@RequiredArgsConstructor
@Service
public class SuccessMatchProducerService {

    @Value("${app.kafka.topics.success_match.name}")
    private String TOPIC_NAME;
    private final KafkaTemplate<String, Match> successMatchTemplate;

    @Transactional(transactionManager = "kafkaTransactionManager")
    public void send(Match successMatches) {

        ListenableFuture<SendResult<String, Match>> future = successMatchTemplate.send(TOPIC_NAME, successMatches);
        future.addCallback(
                new KafkaSendCallback<>(){

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
                }
        );
    }
}
