package com.thy.aodb.scheduler.util;

import org.slf4j.MDC;

public class MDCImpl {
	
	public static final String JOB_NAME = "source";
	
	public static void init(String jobName){
		MDC.clear();
		MDC.put(MDCImpl.JOB_NAME, jobName);
	}
}
