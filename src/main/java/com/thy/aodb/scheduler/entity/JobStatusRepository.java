package com.thy.aodb.scheduler.entity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JobStatusRepository extends JpaRepository<JobStatus, Long> {
	
	JobStatus findTopByNameAndStatusOrderByStartDesc(String jobName, String status);
	
	List<JobStatus> findTop50ByNameOrderByStartDesc(String jobName);

}
