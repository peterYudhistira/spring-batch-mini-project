package one.bca.batch_mini_project.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class JobConfiguration {
    private final JobRepository jobRepository;
    private final AttendanceLogConfiguration attendanceLogConfiguration;
    private final UpdateEmployeeConfiguration updateEmployeeConfiguration;
    private final ReportConfiguration reportConfiguration;

    public JobConfiguration(
            JobRepository jobRepository,
            AttendanceLogConfiguration attendanceLogConfiguration,
            UpdateEmployeeConfiguration updateEmployeeConfiguration,
            ReportConfiguration reportConfiguration
    ) {
        this.jobRepository = jobRepository;
        this.attendanceLogConfiguration = attendanceLogConfiguration;
        this.updateEmployeeConfiguration = updateEmployeeConfiguration;
        this.reportConfiguration = reportConfiguration;
    }

    @Bean
    public Job employeeAttendanceJob() throws Exception {
        return new JobBuilder("reportingJob", jobRepository)
                .start(attendanceLogConfiguration.calculateWorkingHoursStep())
                .next(updateEmployeeConfiguration.updateEmployeeStep())
                .next(reportConfiguration.generateReportStep())
                .build();
    }

    @Bean(name = "asyncTaskExecutor")
    public static AsyncTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        executor.setThreadNamePrefix("async-executor");
        executor.initialize();
        return executor;
    }
}
