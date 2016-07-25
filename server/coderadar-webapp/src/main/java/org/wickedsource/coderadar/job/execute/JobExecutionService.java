package org.wickedsource.coderadar.job.execute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wickedsource.coderadar.CoderadarConfiguration;
import org.wickedsource.coderadar.job.JobLogger;
import org.wickedsource.coderadar.job.core.Job;
import org.wickedsource.coderadar.job.core.ProcessingStatus;
import org.wickedsource.coderadar.job.core.ResultStatus;
import org.wickedsource.coderadar.job.queue.JobDeletedException;
import org.wickedsource.coderadar.job.queue.JobQueueService;
import org.wickedsource.coderadar.job.queue.JobUpdater;

import java.util.Date;

@Service
@Transactional
@ConditionalOnProperty(CoderadarConfiguration.SLAVE)
class JobExecutionService {

    private JobLogger jobLogger = new JobLogger();

    private JobQueueService queueService;

    private JobUpdater jobUpdater;

    private JobExecutor executor;

    @Autowired
    public JobExecutionService(JobQueueService queueService, JobUpdater jobUpdater, JobExecutor executor) {
        this.queueService = queueService;
        this.jobUpdater = jobUpdater;
        this.executor = executor;
    }

    /**
     * Loads the next job from the queue and passes it to the executor to process it. The job will be marked as
     * successfully processed after execution. If an exception occurs, the job will be marked as failed but processed.
     */
    @Scheduled(fixedDelay = CoderadarConfiguration.TIMER_INTERVAL)
    public void executeNextJobInQueue() {
        if (queueService.isQueueEmpty()) {
            jobLogger.emptyQueue();
            return;
        }
        Job job = queueService.dequeue();
        if (job == null) {
            jobLogger.couldNotObtainJob();
        } else {
            jobLogger.startingJob(job);
            try {
                Date startDate = new Date();
                executor.execute(job);
                job.setStartDate(startDate);
                job.setEndDate(new Date());
                job.setProcessingStatus(ProcessingStatus.PROCESSED);
                job.setResultStatus(ResultStatus.SUCCESS);
                jobUpdater.updateJob(job);
                jobLogger.successfullyFinishedJob(job);
            } catch (JobDeletedException e) {
                // the job was deleted while it was processing
                // transaction must be rolled back
                throw e;
            } catch (Throwable e) {
                job.setResultStatus(ResultStatus.FAILED);
                job.setProcessingStatus(ProcessingStatus.PROCESSED);
                job.setEndDate(new Date());
                job.setMessage(String.format("Failed due to exception of type %s. View the log file for details.", e.getClass()));
                // storing the failed job back into the database in a separate transaction because the current
                // transaction may be marked for rollback
                jobUpdater.updateJob(job);
                jobLogger.jobFailed(job, e);
            }
        }
    }
}