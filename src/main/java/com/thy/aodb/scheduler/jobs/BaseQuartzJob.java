package com.thy.aodb.scheduler.jobs;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

import org.quartz.DateBuilder;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.thy.aodb.scheduler.entity.JobStatus;
import com.thy.aodb.scheduler.entity.JobStatusRepository;
import com.thy.aodb.scheduler.util.JobConstants;
/**
 * @author O_ERTURK2
 * */
public abstract class BaseQuartzJob extends QuartzJobBean {
	protected transient Logger LOGGER = LoggerFactory.getLogger(getClass());

	protected ApplicationContext applicationContext;
	
	protected boolean jobDisabled;
	
	protected int retryCount;
	protected int retryIntervalInMinutes;
	protected boolean retryOnException;
	protected boolean chainOnException;
	protected int chainIntervalInMinutes;
	
	@Autowired
	public JobStatusRepository jobStatusRepository;

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		JobStatus savedStatus = null;
		try {
			
			try {
				JobStatus status = new JobStatus();
				status.setName(context.getJobDetail().getKey().toString());
				status.setStatus(JobConstants.JOBSTATUS_STARTED);
				savedStatus = jobStatusRepository.save(status);
			} catch (Exception e) {
				LOGGER.error("Exception occured while saving history detail jobName={} (e={})", context.getJobDetail().getKey(), e.getMessage());
			}
			if (!jobDisabled) {
				performAction(context);
			} else {
				LOGGER.info("Job Disabled={}", context.getJobDetail().getKey().toString());
			}
			if (isRetryOnException() && context.getScheduler().checkExists(getRetryJobKey())) {
				LOGGER.debug("Deleting RetryJob={}", getRetryJobKey());
				context.getScheduler().deleteJob(getRetryJobKey());
			}
			
			try {
				if(savedStatus != null){
					savedStatus.setStatus(JobConstants.JOBSTATUS_SUCCESS);
					jobStatusRepository.save(savedStatus);
				}
			} catch (Exception e) {
				LOGGER.error("Exception occured while saving history detail jobName={} (e={})", context.getJobDetail().getKey(), e.getMessage());
			}
			
		} catch (Exception e) {
			
			try {
				if(savedStatus != null){
					savedStatus.setStatus(JobConstants.JOBSTATUS_EXCEPTION + e.getMessage());
					jobStatusRepository.save(savedStatus);
				}
			} catch (Exception e1) {
				LOGGER.error("Exception occured while saving history detail jobName={} (e={})", context.getJobDetail().getKey(), e1.getMessage());
			}
			
			LOGGER.error("Exception jobName={} (e={})", context.getJobDetail().getKey(), e.getMessage());
			if (isRetryOnException()) {
				try {
					if(!JobConstants.DEFAULT_RETRY_TRIGGER_GROUP.equals(context.getJobDetail().getKey().getGroup())){
						context.getScheduler().deleteJob(getRetryJobKey());
						rescheduleJob(context);
					}else{
						if(context.getTrigger() instanceof SimpleTriggerImpl){
							SimpleTriggerImpl simpleTriggerImpl = (SimpleTriggerImpl)context.getTrigger(); 
							LOGGER.info("Retrying {}, Times Triggered={}, RepeatCount={} ", context.getJobDetail().getKey(), simpleTriggerImpl.getTimesTriggered(), simpleTriggerImpl.getRepeatCount());
							if(simpleTriggerImpl.getTimesTriggered()>simpleTriggerImpl.getRepeatCount()){
								LOGGER.error("Retry job {} failed to succeed !!!", context.getJobDetail().getKey());
							}
						}
					}
				} catch (SchedulerException e1) {
					LOGGER.error(e1.getMessage());
					throw new JobExecutionException(e1);
				}
			}
			throw new JobExecutionException(e);
		}
	}

	private void rescheduleJob(JobExecutionContext context) throws SchedulerException {
		SimpleTrigger simpleRetryTrigger = (SimpleTrigger) TriggerBuilder.newTrigger()
				.withIdentity(getRetryTriggerName(context), JobConstants.DEFAULT_RETRY_TRIGGER_GROUP)
				.startAt(DateBuilder.futureDate(getRetryIntervalInMinutes(), IntervalUnit.MINUTE))
				.withSchedule(simpleSchedule()
			            .withIntervalInMinutes(getRetryIntervalInMinutes())
			            .withRepeatCount(getRetryCount()))
				.forJob(getRetryJobKey())
				.build();
		JobDetail retryJob = JobBuilder.newJob(this.getClass()).requestRecovery(true).withIdentity(getRetryJobKey())
				.usingJobData(context.getJobDetail().getJobDataMap()).storeDurably()
				.build();
		context.getScheduler().scheduleJob(retryJob, simpleRetryTrigger);
		return;
	}

	protected abstract void performAction(JobExecutionContext context) throws JobExecutionException;

	public abstract JobKey getRetryJobKey();
	
	public String getRetryTriggerName(JobExecutionContext context){
		return JobConstants.DEFAULT_RETRY_PREFIX.concat(context.getJobDetail().getKey().getName());
	}

	public int getRetryCount() {
		return retryCount-1;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public int getRetryIntervalInMinutes() {
		return retryIntervalInMinutes;
	}

	public void setRetryIntervalInMinutes(int retryIntervalInMinutes) {
		this.retryIntervalInMinutes = retryIntervalInMinutes;
	}

	public boolean isRetryOnException() {
		return retryOnException;
	}

	public void setRetryOnException(boolean retryOnException) {
		this.retryOnException = retryOnException;
	}

	public boolean isChainOnException() {
		return chainOnException;
	}

	public void setChainOnException(boolean chainOnException) {
		this.chainOnException = chainOnException;
	}

	public int getChainIntervalInMinutes() {
		return chainIntervalInMinutes;
	}

	public void setChainIntervalInMinutes(int chainIntervalInMinutes) {
		this.chainIntervalInMinutes = chainIntervalInMinutes;
	}

	public boolean isJobDisabled() {
		return jobDisabled;
	}

	public void setJobDisabled(boolean jobDisabled) {
		this.jobDisabled = jobDisabled;
	}

}