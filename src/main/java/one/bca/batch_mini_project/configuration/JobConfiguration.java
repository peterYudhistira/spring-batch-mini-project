package one.bca.batch_mini_project.configuration;

import one.bca.batch_mini_project.model.Report;
import one.bca.batch_mini_project.objectmapper.ReportRowMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;

@Configuration
public class JobConfiguration {
    private final JobRepository jobRepository;
    private final AttendanceLogConfiguration attendanceLogConfiguration;
    private final ReportConfiguration reportConfiguration;

    public JobConfiguration(
            JobRepository jobRepository,
            AttendanceLogConfiguration attendanceLogConfiguration,
            ReportConfiguration reportConfiguration
    ) {
        this.jobRepository = jobRepository;
        this.attendanceLogConfiguration = attendanceLogConfiguration;
        this.reportConfiguration = reportConfiguration;
    }

    public Job employeeAttendanceJob() throws Exception {
        return new JobBuilder("ReportingJob", jobRepository)
                .start(attendanceLogConfiguration.calculateWorkingHoursStep())
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
