package one.bca.batch_mini_project.configuration;

import one.bca.batch_mini_project.model.Attendance;
import one.bca.batch_mini_project.model.AttendanceLog;
import one.bca.batch_mini_project.processor.AttendanceLogProcessor;
import one.bca.batch_mini_project.readers.AttendanceLogCsvReader;
import one.bca.batch_mini_project.writers.AttendanceLogWriter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
public class AttendanceLogConfiguration {

    private final JobRepository jobRepository;
    private final DataSourceTransactionManager transactionManager;
    private final AttendanceLogCsvReader attendanceLogCsvReader;
    private final AttendanceLogProcessor attendanceLogProcessor;
    private final AttendanceLogWriter attendanceLogWriter;

    public AttendanceLogConfiguration(
            JobRepository jobRepository,
            DataSourceTransactionManager transactionManager,
            AttendanceLogCsvReader attendanceLogCsvReader,
            AttendanceLogProcessor attendanceLogProcessor,
            AttendanceLogWriter attendanceLogWriter
    ) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.attendanceLogCsvReader = attendanceLogCsvReader;
        this.attendanceLogProcessor = attendanceLogProcessor;
        this.attendanceLogWriter = attendanceLogWriter;
    }

    @Bean
    public Step calculateWorkingHoursStep() throws Exception {
        return new StepBuilder("calculateWorkingHoursStep", jobRepository)
                .<Attendance, AttendanceLog>chunk(10, transactionManager)
                .reader(attendanceLogCsvReader.csvAttendanceReader())
                .processor(attendanceLogProcessor.processAttendance())
                .writer(attendanceLogWriter.itemWriter(transactionManager.getDataSource()))
                .taskExecutor(JobConfiguration.taskExecutor())
                .build();
    }
}
