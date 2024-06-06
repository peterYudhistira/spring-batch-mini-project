package one.bca.batch_mini_project;

import one.bca.batch_mini_project.configuration.AttendanceConfiguration;
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
public class BatchScheduler {
    private final JobLauncher jobLauncher;

    private final AttendanceConfiguration attendanceConfiguration;

    public BatchScheduler(JobLauncher jobLauncher, AttendanceConfiguration attendanceConfiguration) {
        this.jobLauncher = jobLauncher;
        this.attendanceConfiguration = attendanceConfiguration;
    }

    @Scheduled(cron="0 0 0 1 1/1 *")
    public void runJob(){
        try{
            System.out.println("Running Batch Job!");
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(attendanceConfiguration.attendanceJob(), jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }
}
