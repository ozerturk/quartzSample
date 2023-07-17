package com.thy.aodb.scheduler.api;

import java.util.Date;

public class JobDto {

	private String jobName;
	private	String jobGroup;
	private Date nextFireTime;
	private Date lastFireTime;
	private Date fireTime;
	private String cronExpression;

	public JobDto(String jobName, String jobGroup, Date fireTime, Date lastFireTime, Date nextFireTime, String cronExpression) {
		this.jobName = jobName;
		this.jobGroup = jobGroup;
		this.fireTime = fireTime;
		this.nextFireTime = nextFireTime;
		this.lastFireTime = lastFireTime;
		this.cronExpression = cronExpression;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	public Date getNextFireTime() {
		return nextFireTime;
	}

	public void setNextFireTime(Date nextFireTime) {
		this.nextFireTime = nextFireTime;
	}

	public Date getLastFireTime() {
		return lastFireTime;
	}

	public void setLastFireTime(Date lastFireTime) {
		this.lastFireTime = lastFireTime;
	}

	public Date getFireTime() {
		return fireTime;
	}

	public void setFireTime(Date fireTime) {
		this.fireTime = fireTime;
	}

	public String getCronExpression() {
		return cronExpression;
	}
	
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("JobDto [jobName=").append(jobName).append(", jobGroup=").append(jobGroup).append(", nextFireTime=").append(nextFireTime).append(", lastFireTime=").append(lastFireTime).append(", fireTime=").append(fireTime).append(", cronExpression=").append(cronExpression).append("]");
		return builder.toString();
	}
	
}
