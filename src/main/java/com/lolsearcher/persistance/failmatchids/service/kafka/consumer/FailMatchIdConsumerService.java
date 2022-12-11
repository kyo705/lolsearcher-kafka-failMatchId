package com.lolsearcher.persistance.failmatchids.service.kafka.consumer;

import com.lolsearcher.persistance.failmatchids.model.entity.match.Match;
import com.lolsearcher.persistance.failmatchids.service.api.RiotGamesApiService;
import com.lolsearcher.persistance.failmatchids.service.kafka.producer.SuccessMatchProducerService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FailMatchIdConsumerService {

    private final RiotGamesApiService riotGamesApiService;
    private final SuccessMatchProducerService successMatchProducerService;

    /**
     * 해당 애플리케이션 시작점
     */
    @KafkaListener(
            topics = {"${app.kafka.topics.filtered_fail_match.name}"},
            groupId = "${app.kafka.consumers.filtered_fail_match.group_id}",
            containerFactory = "${app.kafka.consumers.filtered_fail_match.container_factory}"
    )
    public void processFailMatchIds(ConsumerRecords<String, String> failMatchIdRecords) {
        List<String> failMatchIds = getFailMatchIds(failMatchIdRecords);

        List<Match> successMatches = riotGamesApiService.requestMatches(failMatchIds);

        successMatchProducerService.send(successMatches);
    }

    private List<String> getFailMatchIds(ConsumerRecords<String, String> failMatchIdRecords) {
        List<String> failMatchIds = new ArrayList<>(failMatchIdRecords.count());

        for(ConsumerRecord<String, String> failMatchIdRecord : failMatchIdRecords){
            failMatchIds.add(failMatchIdRecord.value());
        }
        return failMatchIds;
    }
}
