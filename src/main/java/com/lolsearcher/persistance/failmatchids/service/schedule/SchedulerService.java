package com.lolsearcher.persistance.failmatchids.service.schedule;

import com.lolsearcher.persistance.failmatchids.scheduler.info.Timer;
import com.lolsearcher.persistance.failmatchids.scheduler.util.TimerUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@RequiredArgsConstructor
@Service
public class SchedulerService {

    private final Scheduler scheduler;

    @PostConstruct
    public void init() throws SchedulerException {
        scheduler.start();
    }

    @PreDestroy
    public void preDestroy() throws SchedulerException {
        scheduler.shutdown();
    }

    public void schedule(Class<? extends Job> jobclass, Timer timer) throws SchedulerException {
        JobDetail jobDetail = TimerUtils.buildJobDetail(jobclass, timer);
        Trigger trigger = TimerUtils.buildTriggerWithSimpleSchedule(timer);

        scheduler.scheduleJob(jobDetail, trigger);
    }
}
