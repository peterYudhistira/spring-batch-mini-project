package one.bca.batch_mini_project.configuration;

import lombok.Data;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@Data
public class JobConfiguration {
    private final JobRepository jobRepository;
    private final EmployeeConfiguration employeeConfiguration;
    private final AttendanceConfiguration attendanceConfiguration;
    private final EmployeeAttendanceConfiguration employeeAttendanceConfiguration;


    // ini job yang dipanggil
    public Job employeeAttendanceJob() throws Exception {
        return new JobBuilder("ReportingJob", jobRepository)
                .start(employeeConfiguration.getEmployeeStep())
                .next(attendanceConfiguration.getAttendanceStep())
                .next(employeeAttendanceConfiguration.updateDBStep())
                .next(employeeAttendanceConfiguration.generateReportStep())
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