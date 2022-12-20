package com.lolsearcher.persistance.failmatchids.service.consumer;

import com.lolsearcher.persistance.failmatchids.model.entity.match.Match;
import com.lolsearcher.persistance.failmatchids.service.api.RiotGamesApiService;
import com.lolsearcher.persistance.failmatchids.service.kafka.producer.SuccessMatchProducerService;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;

@EmbeddedKafka(partitions = 3,
        brokerProperties = {
            "listeners=PLAINTEXT://localhost:9092",
            "offsets.topic.replication.factor=1",
            "transaction.state.log.replication.factor=1",
            "transaction.state.log.min.isr=1"}
        )
@SpringBootTest
@DirtiesContext
public class FailMatchIdConsumerServiceUnitTest {

    @MockBean
    private RiotGamesApiService riotGamesApiService;
    @MockBean
    private SuccessMatchProducerService successMatchProducerService;

    @DisplayName("@kafkaListener를 통해 토픽 내 레코드가 컨슈밍된다.")
    @Test
    public void processFailMatchIdsTest() throws ExecutionException, InterruptedException {
        //given
        List<ProducerRecord<String, String>> producerRecords = FailMatchIdConsumerTestSetUp.getFailMatchIdRecord();

        for(ProducerRecord<String, String> producerRecord : producerRecords){
            String matchId = producerRecord.value();

            Match match = new Match();
            match.setMatchId(matchId);

            given(riotGamesApiService.requestMatch(matchId)).willReturn(match);

            given(successMatchProducerService.createProducerRecord(match))
                    .willReturn(new ProducerRecord<>("success_match", match));
        }

        //when
        FailMatchIdConsumerTestSetUp.sendProducerRecords(producerRecords);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //then
        for(ProducerRecord<String, String> producerRecord : producerRecords){
            verify(riotGamesApiService, times(1)).requestMatch(producerRecord.value());
        }
        verify(successMatchProducerService, times(producerRecords.size())).send(any(ProducerRecord.class));
    }
}
