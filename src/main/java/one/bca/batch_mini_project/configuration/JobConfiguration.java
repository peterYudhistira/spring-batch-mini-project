package one.bca.batch_mini_project.configuration;

import lombok.Data;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

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
}
