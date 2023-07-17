package com.thy.aodb.scheduler.jobs;

import java.util.HashMap;
import java.util.Map;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;

import com.thy.aodb.scheduler.entity.FlightInfo;
import com.thy.aodb.scheduler.support.RestTemplateFactory;
import com.thy.aodb.scheduler.util.JobConstants;

@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class FlightInfoJob extends BaseQuartzJob{
	
	private static final JobKey retryJobKey = JobKey.jobKey(
			JobConstants.DEFAULT_RETRY_PREFIX.concat(FlightInfoJob.class.getSimpleName()),
			JobConstants.DEFAULT_RETRY_TRIGGER_GROUP);
	
	@Autowired
	RestTemplateFactory restTemplateFactory;
	
	private String jobName;
	
	private String aodbFeedOutFlightInfoWSUrl;
	
	@Override
	protected void performAction(JobExecutionContext context) throws JobExecutionException {
		
		FlightInfo flightInfoForInsert = new FlightInfo();
		flightInfoForInsert.setTitle("Hello ");
		flightInfoForInsert.setDescription("Hello Description");
		
		Map<String, String> vars = new HashMap<>();
		
		FlightInfo postForObject = restTemplateFactory.getObject().postForObject(aodbFeedOutFlightInfoWSUrl, flightInfoForInsert, FlightInfo.class, vars);
		
        LOGGER.info(postForObject!=null ? "Response : "+postForObject.toString():"Empty Response");
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getAodbFeedOutFlightInfoWSUrl() {
		return aodbFeedOutFlightInfoWSUrl;
	}

	public void setAodbFeedOutFlightInfoWSUrl(String aodbFeedOutFlightInfoWSUrl) {
		this.aodbFeedOutFlightInfoWSUrl = aodbFeedOutFlightInfoWSUrl;
	}

	@Override
	public JobKey getRetryJobKey() {
		return retryJobKey;
	}

}
