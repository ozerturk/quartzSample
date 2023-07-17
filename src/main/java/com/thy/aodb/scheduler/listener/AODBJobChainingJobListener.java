package com.thy.aodb.scheduler.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.DateBuilder;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.listeners.JobListenerSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thy.aodb.scheduler.util.JobConstants;

public class AODBJobChainingJobListener extends JobListenerSupport {

	protected transient Logger LOGGER = LoggerFactory.getLogger(getClass());

	private String name;
	private Map<JobDetail, List<JobDetail>> chainLinks = new HashMap<>();

	/**
	 * When the Job identified by the key completes;
	 * List of jobs identified by the key will be triggered.
	 */
	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {

		List<JobDetail> sj = chainLinks.get(context.getJobDetail());

		if (sj == null) {
			return;
		}
		
		if(jobException!=null && !context.getJobDetail().getJobDataMap().getBoolean(JobConstants.chainOnException)){
			LOGGER.debug("Job Chaining not enabled on exception for Key = {}", context.getJobDetail());
			return;
		}

		getLog().info("Job '" + context.getJobDetail().getKey() + "' will now chain to Job '" + sj + "'");

		for (JobDetail jobdetail : sj) {
			try {
				scheduleChainedJob(context, jobdetail.getKey());
				LOGGER.debug("Job Chained with Key = {}", jobdetail.getKey());
			} catch (SchedulerException se) {
				LOGGER.error("Error encountered during chaining to Job '" + sj + "'", se);
			}
		}
	}
	
	private void scheduleChainedJob(JobExecutionContext context, JobKey jobkey) throws SchedulerException {
		SimpleTrigger simpleRetryTrigger = (SimpleTrigger) TriggerBuilder.newTrigger()
				.withIdentity(getChainTriggerName(jobkey), JobConstants.DEFAULT_CHAIN_TRIGGER_GROUP)
				.startAt(DateBuilder.futureDate(getChainIntervalInMinutes(context), IntervalUnit.MINUTE))
				.forJob(jobkey)
				.build();
		context.getScheduler().scheduleJob(simpleRetryTrigger);
		return;
	}
	
	public String getChainTriggerName(JobKey jobkey){
		return JobConstants.DEFAULT_CHAIN_PREFIX.concat(jobkey.getName());
	}
	
	public int getChainIntervalInMinutes(JobExecutionContext context){
		return context.getJobDetail().getJobDataMap().getInt(JobConstants.chainIntervalInMinutes);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<JobDetail, List<JobDetail>> getChainLinks() {
		return chainLinks;
	}

	public void setChainLinks(Map<JobDetail, List<JobDetail>> chainLinks) {
		this.chainLinks = chainLinks;
	}

}