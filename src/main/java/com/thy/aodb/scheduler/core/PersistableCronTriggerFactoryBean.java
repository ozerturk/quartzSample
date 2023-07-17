package com.thy.aodb.scheduler.core;

import java.text.ParseException;

import org.springframework.scheduling.quartz.CronTriggerFactoryBean;

public class PersistableCronTriggerFactoryBean extends CronTriggerFactoryBean {
	
	public static final String JOB_DETAIL_KEY = "jobDetail";
	
	@Override
	public void afterPropertiesSet() throws ParseException {
		super.afterPropertiesSet();

		// Remove the JobDetail element
		getJobDataMap().remove(JOB_DETAIL_KEY);
	}
}