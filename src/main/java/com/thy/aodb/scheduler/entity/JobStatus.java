package com.thy.aodb.scheduler.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="QRTZ_JOB_HISTORY")
@SequenceGenerator(name="SEQ_GENERATOR", sequenceName="QRTZ_JOB_HISTORY_SEQ",allocationSize=1)
@Data
public class JobStatus {
	
	@Id
	@Column(name = "ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GENERATOR")
	private Long id;
	
	@Column(name="JOB_NAME")
	private String name;
	
	@Column(name="START_DATE")
	private Timestamp start;
	
	@Column(name="FINISH_DATE")
	private Timestamp finish;

	@Column(name="STATUS")
	private String status;


}
