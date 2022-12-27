package com.lolsearcher.persistance.failmatchids.service.schedule;

import com.lolsearcher.persistance.failmatchids.constant.FailMatchIdConsumerConstants;
import com.lolsearcher.persistance.failmatchids.scheduler.info.Timer;
import com.lolsearcher.persistance.failmatchids.scheduler.job.ConsumerResumeJob;
import com.lolsearcher.persistance.failmatchids.service.kafka.consumer.ConsumerWorkService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.context.EmbeddedKafka;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
public class SchedulerServiceIntegrationTest {

    @MockBean
    private ConsumerWorkService consumerWorkService;

    @Autowired
    private SchedulerService schedulerService;

    @DisplayName("스케줄러가 호출한 Job이 설정한 JobDetail과 trigger에 의해 정상적으로 작동한다.")
    @Test
    public void scheduleTest() throws SchedulerException, InterruptedException {

        //given
        Timer timer = new Timer();
        timer.setTotalFireCount(5);
        timer.setRunForever(false);
        timer.setInitialOffsetMs(0);
        timer.setRepeatIntervalMs(100);
        timer.setCallbackData(FailMatchIdConsumerConstants.LISTENER_ID);

        //when
        schedulerService.schedule(ConsumerResumeJob.class, timer);

        Thread.sleep(2000);

        //then
        verify(consumerWorkService, times(timer.getTotalFireCount())).resume(any());
    }
}
