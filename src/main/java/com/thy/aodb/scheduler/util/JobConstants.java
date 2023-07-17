package com.thy.aodb.scheduler.util;

public class JobConstants {

	public static final String dateFormat_yyyyMMdd 		= "yyyyMMdd";
	public static final String dateFormat_yyyyMMddHHmm 	= "yyyyMMddHHmm";


	public static final String retryCount 				= "retryCount";
	public static final String retryIntervalInMinutes 	= "retryIntervalInMinutes";
	public static final String retryOnException 		= "retryOnException";
	public static final String chainOnException 		= "chainOnException";
	public static final String chainIntervalInMinutes 	= "chainIntervalInMinutes";

	
	public static final String DEFAULT_RETRY_TRIGGER_GROUP 	= "RETRY";
	public static final String DEFAULT_RETRY_PREFIX 		= "RETRY_";

	public static final String DEFAULT_CHAIN_TRIGGER_GROUP 	= "CHAIN";
	public static final String DEFAULT_CHAIN_PREFIX 		= "CHAIN_";

	public static final String JOBSTATUS_SUCCESS 	= "SUCCESS";
	public static final String JOBSTATUS_STARTED 	= "STARTED";
	public static final String JOBSTATUS_EXCEPTION 	= "EXCEPTION ";
	public static final String ACT 					= "ACT";
	public static final String EST 					= "EST";
	public static final String SCHED 				= "SCHED";

	public static final String SECRET = "secret";

}
