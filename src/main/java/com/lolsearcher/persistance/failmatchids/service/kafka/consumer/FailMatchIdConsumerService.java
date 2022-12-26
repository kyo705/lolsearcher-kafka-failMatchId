package com.lolsearcher.persistance.failmatchids.service.kafka.consumer;

import com.lolsearcher.persistance.failmatchids.constant.FailMatchIdConsumerConstants;
import com.lolsearcher.persistance.failmatchids.constant.FailMatchIdTopicConstants;
import com.lolsearcher.persistance.failmatchids.constant.RiotGamesConstants;
import com.lolsearcher.persistance.failmatchids.model.entity.match.Match;
import com.lolsearcher.persistance.failmatchids.service.api.RiotGamesApiService;
import com.lolsearcher.persistance.failmatchids.service.kafka.producer.SuccessMatchProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@RequiredArgsConstructor
@Service
public class FailMatchIdConsumerService {

    private final RiotGamesApiService riotGamesApiService;
    private final SuccessMatchProducerService successMatchProducerService;

    /**
     * 해당 애플리케이션 시작점
     */
    @KafkaListener(
            topics = {FailMatchIdTopicConstants.TOPIC_NAME},
            id = FailMatchIdConsumerConstants.LISTENER_ID,
            groupId = FailMatchIdConsumerConstants.GROUP_ID,
            containerFactory = FailMatchIdConsumerConstants.LISTENER_CONTAINER_FACTORY_NAME
    )
    public void processFailMatchIds(ConsumerRecord<String, String> failMatchIdRecord, Acknowledgment acknowledgment) {

        String failMatchId = failMatchIdRecord.value();
        try{
            Match successMatch = riotGamesApiService.requestMatch(failMatchId);

            ProducerRecord<String, Match> successMatchRecord
                    = successMatchProducerService.createProducerRecord(successMatch);

            successMatchProducerService.send(successMatchRecord); //send 실패시 해당 서비스 트랜잭션 롤백
            acknowledgment.acknowledge();

        }catch (WebClientResponseException e){
            if(e.getStatusCode() != HttpStatus.TOO_MANY_REQUESTS){
                throw e;
            }
            try {
                Thread.sleep(2*60*1000);
            } catch (InterruptedException ex) {
                log.error(ex.getMessage());
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
