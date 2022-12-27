package com.lolsearcher.persistance.failmatchids.service.consumer;

import com.lolsearcher.persistance.failmatchids.constant.FailMatchIdConsumerConstants;
import com.lolsearcher.persistance.failmatchids.model.entity.match.Match;
import com.lolsearcher.persistance.failmatchids.service.api.RiotGamesApiService;
import com.lolsearcher.persistance.failmatchids.service.kafka.consumer.ConsumerWorkService;
import com.lolsearcher.persistance.failmatchids.service.kafka.producer.SuccessMatchProducerService;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@EmbeddedKafka(
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:9092",
                "offsets.topic.replication.factor=1",
                "transaction.state.log.replication.factor=1",
                "transaction.state.log.min.isr=1"}
)
@EnableAutoConfiguration(
        excludeName = {
                "failMatchIdsTopic",
                "successMatchesTopic"
        }
)
@SpringBootTest
@DirtiesContext
public class ConsumerWorkServiceIntegrationTest {

    @MockBean
    private RiotGamesApiService riotGamesApiService;
    @MockBean
    private SuccessMatchProducerService successMatchProducerService;

    @Autowired
    private ConsumerWorkService consumerWorkService;

    @Autowired
    private KafkaListenerEndpointRegistry registry;

    @DisplayName("lisnter Container를 pause할 경우 컨슈머는 poll()시 컨슈머레코드를 Fetcher로부터 얻지 못한다.")
    @Test
    public void pauseTest() throws ExecutionException, InterruptedException {

        //given
        List<ProducerRecord<String, String>> producerRecords = FailMatchIdConsumerTestSetUp.getFailMatchIdRecord();

        //when
        consumerWorkService.pause(FailMatchIdConsumerConstants.LISTENER_ID);

        FailMatchIdConsumerTestSetUp.sendProducerRecords(producerRecords);

        Thread.sleep(2000);

        //then
        verify(successMatchProducerService, times(0)).send(any(ProducerRecord.class));
        verify(riotGamesApiService, times(0)).requestMatch(anyString());
    }

    @DisplayName("pause된 lisnter Container를 resume할 경우 컨슈머는 poll()시 컨슈머레코드를 Fetcher로부터 얻는다.")
    @Test
    public void resumeTest() throws ExecutionException, InterruptedException {

        //given
        Match match = new Match();
        given(riotGamesApiService.requestMatch(any())).willReturn(match);

        ProducerRecord<String, Match> record = new ProducerRecord<>("임시 토픽", match);
        given(successMatchProducerService.createProducerRecord(any())).willReturn(record);

        consumerWorkService.pause(FailMatchIdConsumerConstants.LISTENER_ID);

        List<ProducerRecord<String, String>> producerRecords = FailMatchIdConsumerTestSetUp.getFailMatchIdRecord();
        FailMatchIdConsumerTestSetUp.sendProducerRecords(producerRecords);

        Thread.sleep(2000);
        /* pause 시 컨슈머가 poll()을 호출해도 레코드를 얻지 못한다 */
        verify(successMatchProducerService, times(0)).send(any());
        verify(riotGamesApiService, times(0)).requestMatch(anyString());

        //when
        consumerWorkService.resume(FailMatchIdConsumerConstants.LISTENER_ID);

        Thread.sleep(2000);

        //then
        verify(successMatchProducerService, atLeastOnce()).send(any());
        verify(riotGamesApiService, atLeastOnce()).requestMatch(anyString());
    }

    @DisplayName("lisnter Container를 destory할 경우 해당 리스너 컨테이너는 제거된다.")
    @Test
    public void destroyTest() throws InterruptedException {

        //given
        MessageListenerContainer beforeListenerContainer = registry.getListenerContainer(FailMatchIdConsumerConstants.LISTENER_ID);

        assertThat(requireNonNull(beforeListenerContainer).isRunning()).isTrue();

        //when
        consumerWorkService.destroy(FailMatchIdConsumerConstants.LISTENER_ID);

        Thread.sleep(2000);

        //then
        MessageListenerContainer afterListenerContainer = registry.getListenerContainer(FailMatchIdConsumerConstants.LISTENER_ID);

        assertThat(afterListenerContainer).isNull();
    }
}
