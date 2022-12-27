package com.lolsearcher.persistance.failmatchids.scheduler.job;

import com.lolsearcher.persistance.failmatchids.scheduler.info.Timer;
import com.lolsearcher.persistance.failmatchids.service.kafka.consumer.ConsumerWorkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ConsumerResumeJob implements Job {

    private final ConsumerWorkService consumerWorkService;

    @Override
    public void execute(JobExecutionContext context) {

        String jobDetailKey = context.getJobDetail().getKey().getName();

        String listenerId = ((Timer)context.getJobDetail().getJobDataMap().get(jobDetailKey)).getCallbackData();

        consumerWorkService.resume(listenerId);
    }
}
