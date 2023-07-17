package com.thy.aodb.scheduler.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thy.aodb.scheduler.entity.JobStatus;
import com.thy.aodb.scheduler.entity.JobStatusRepository;


@Controller
@RequestMapping("/admin/job")
public class JobController  {

	@Autowired JobStatusRepository jobStatusRepository;

	@Autowired Scheduler scheduler;

	@RequestMapping(value = "/current", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<JobDto>> current() throws SchedulerException {
		List<JobDto> dtoList = new ArrayList<>();
		
		for (JobExecutionContext jobContext : scheduler.getCurrentlyExecutingJobs()) {
			JobKey jobKey = jobContext.getJobDetail().getKey();
			dtoList.add(new JobDto(jobKey.getName(), jobKey.getGroup(), jobContext.getFireTime(), jobContext.getPreviousFireTime(), jobContext.getNextFireTime(), null));
		}

		
		return new ResponseEntity<>(dtoList, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<JobDto>> list() throws SchedulerException {
		List<JobDto> dtoList = new ArrayList<>();
		
		for (String groupName : scheduler.getJobGroupNames()) {

			// get jobkey
			for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {

				String jobName = jobKey.getName();
				String jobGroup = jobKey.getGroup();

				// get job's trigger
				List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
				Date nextFireTime = null;
				Date previousFireTime = null;
				if(!triggers.isEmpty()){
					Trigger trigger = triggers.get(0);
					nextFireTime = trigger.getNextFireTime();
					previousFireTime = trigger.getPreviousFireTime();
				}

				dtoList.add(new JobDto(jobName, jobGroup, null, previousFireTime, nextFireTime, null));
			}

		}
		
		return new ResponseEntity<>(dtoList, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/history/{page}/{count}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Page<JobStatus>> history(@PathVariable int page, @PathVariable int count) {
		Page<JobStatus> findAll = jobStatusRepository.findAll(PageRequest.of(page, count, Direction.DESC, "start"));
		return new ResponseEntity<>(findAll, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/execute/{jobName}/{jobGroup}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String>  fireNow(@PathVariable String jobName, @PathVariable String jobGroup)
		throws SchedulerException {

		JobKey jobKey = new JobKey(jobName, jobGroup);
		scheduler.triggerJob(jobKey);
		
		return new ResponseEntity<>("DONE", HttpStatus.OK);

	}

}
