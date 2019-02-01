package org.wickedsource.coderadar.job.core;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import org.wickedsource.coderadar.project.domain.Project;

/** The Job entity defines a task in the coderadar application that is run asynchronously. */
@Entity
@Table(name = "job")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "jobType", discriminatorType = DiscriminatorType.STRING)
@SequenceGenerator(name = "job_sequence", sequenceName = "seq_job_id", allocationSize = 1)
public class Job {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "job_sequence")
	@Column(name = "id")
	private Long id;

	@Column(name = "version")
	@Version
	private Integer version;

	@Column(name = "queued_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date queuedDate;

	@Column(name = "start_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;

	@Column(name = "end_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "processing_status", nullable = false)
	private ProcessingStatus processingStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "result_status")
	private ResultStatus resultStatus;

	@Column(name = "message")
	private String message;

	@ManyToOne
	@JoinColumn(name = "project_id")
	private Project project;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getQueuedDate() {
		return queuedDate;
	}

	public void setQueuedDate(Date queuedDate) {
		this.queuedDate = queuedDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public ProcessingStatus getProcessingStatus() {
		return processingStatus;
	}

	public void setProcessingStatus(ProcessingStatus processingStatus) {
		this.processingStatus = processingStatus;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ResultStatus getResultStatus() {
		return resultStatus;
	}

	public void setResultStatus(ResultStatus resultStatus) {
		this.resultStatus = resultStatus;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return String.format("[Job: id=%d; type=%s]", this.id, getClass().getSimpleName());
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
}
