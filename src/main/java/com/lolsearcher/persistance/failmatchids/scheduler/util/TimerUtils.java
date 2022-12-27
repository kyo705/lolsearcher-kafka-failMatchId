package com.lolsearcher.persistance.failmatchids.scheduler.util;

import com.lolsearcher.persistance.failmatchids.scheduler.info.Timer;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;

import java.util.Date;


public final class TimerUtils {

	private TimerUtils() {}
	
	public static JobDetail buildJobDetail(final Class<? extends Job> jobclass, final Timer timerInfo) {
		JobKey jobKey = new JobKey(timerInfo.getCallbackData());
		
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put(jobKey.getName(), timerInfo);

		return (JobDetailImpl)JobBuilder
				.newJob(jobclass)
				.withIdentity(jobKey)
				.setJobData(jobDataMap)
				.build();
	}
	
	public static Trigger buildTriggerWithSimpleSchedule(final Timer timerInfo) {
		SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder
				.simpleSchedule().withIntervalInMilliseconds(timerInfo.getRepeatIntervalMs());
		
		if(timerInfo.isRunForever()) {
			simpleScheduleBuilder.repeatForever();
		}else {
			simpleScheduleBuilder.withRepeatCount(timerInfo.getTotalFireCount()-1);
		}
		
		return TriggerBuilder
				.newTrigger()
				.withIdentity(timerInfo.getCallbackData())
				.forJob(timerInfo.getCallbackData())
				.withSchedule(simpleScheduleBuilder)
				.startAt(new Date(System.currentTimeMillis() + timerInfo.getInitialOffsetMs()))
				.build();
	}
}
