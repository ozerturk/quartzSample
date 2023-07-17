package com.thy.aodb.scheduler.listener;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.listeners.TriggerListenerSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AODBTriggerListener extends TriggerListenerSupport{
	
	protected transient Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Override
	public String getName() {
		return "AODB hello listener";
	}
	
	public void triggerFired(Trigger trigger, JobExecutionContext context) {
		LOGGER.debug("TRIGGER FIRED with Key = {}",trigger.getKey());
	}

	public void triggerMisfired(Trigger trigger) {
		LOGGER.debug("TRIGGER MIS_FIRED");
	}
	
    public void triggerComplete(Trigger trigger, JobExecutionContext context, CompletedExecutionInstruction triggerInstructionCode) {
    	LOGGER.debug("TRIGGER COMPLETED with Key = {}", trigger.getKey());
    }

    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
        return false;
    }

}
