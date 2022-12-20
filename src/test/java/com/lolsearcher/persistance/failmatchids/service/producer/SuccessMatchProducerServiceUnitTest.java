package com.lolsearcher.persistance.failmatchids.service.producer;

import com.lolsearcher.persistance.failmatchids.model.entity.match.Match;
import com.lolsearcher.persistance.failmatchids.service.kafka.producer.SuccessMatchProducerService;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

@EmbeddedKafka(
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:9092",
                "offsets.topic.replication.factor=1",
                "transaction.state.log.replication.factor=1",
                "transaction.state.log.min.isr=1"
        }
)
@EnableAutoConfiguration(
        excludeName = {
                "failMatchIdsTopic",
                "successMatchesTopic"
        }
)
@SpringBootTest
@DirtiesContext
public class SuccessMatchProducerServiceUnitTest {

    @Autowired
    private SuccessMatchProducerService successMatchProducerService;

    @DisplayName("프로듀서 레코드를 seend하면 토픽의 적절한 파티션에 적재된다.")
    @ParameterizedTest
    @MethodSource("com.lolsearcher.persistance.failmatchids.service.producer.SuccessMatchProducerTestSetUp#successMatchRecords")
    public void sendTest(List<ProducerRecord<String, Match>> successMatchRecords) throws ExecutionException, InterruptedException {
        //given

        //when
        for(ProducerRecord<String, Match> record : successMatchRecords){
            successMatchProducerService.send(record);
        }
        Thread.sleep(1000);

        //then
        List<Match> successMatches = SuccessMatchProducerTestSetUp.consumeSuccessMatches();

        assertThat(successMatches.size()).isEqualTo(successMatches.size());
        for(ProducerRecord<String, Match> record : successMatchRecords){
            assertThat(record.value()).isIn(successMatches);
        }
    }
}
