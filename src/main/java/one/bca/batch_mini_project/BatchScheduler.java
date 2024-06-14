package one.bca.batch_mini_project;

import lombok.Data;
import one.bca.batch_mini_project.configuration.JobConfiguration;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Data
public class BatchScheduler {
    private final JobLauncher jobLauncher;

    private final JobConfiguration jobConfiguration;

    public BatchScheduler(JobLauncher jobLauncher, JobConfiguration jobConfiguration) {
        this.jobLauncher = jobLauncher;
        this.jobConfiguration = jobConfiguration;
    }

    @Scheduled(cron="0 0 1 * *")
    public void runJob(){
        try{
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(jobConfiguration.employeeAttendanceJob(), jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
