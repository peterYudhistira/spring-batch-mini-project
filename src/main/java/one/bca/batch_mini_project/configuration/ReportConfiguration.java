package one.bca.batch_mini_project.configuration;

import one.bca.batch_mini_project.model.Report;
import one.bca.batch_mini_project.readers.AttendanceLogDbReader;
import one.bca.batch_mini_project.writers.ReportWriter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
public class ReportConfiguration {
    private final JobRepository jobRepository;
    private final DataSourceTransactionManager transactionManager;
    private final AttendanceLogDbReader attendanceLogDbReader;
    private final ReportWriter reportWriter;

    public ReportConfiguration(
            JobRepository jobRepository,
            DataSourceTransactionManager transactionManager,
            AttendanceLogDbReader attendanceLogDbReader,
            ReportWriter reportWriter
    ) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.attendanceLogDbReader = attendanceLogDbReader;
        this.reportWriter = reportWriter;
    }

    @Bean
    public Step generateReportStep() throws Exception {
        return new StepBuilder("generateReportStep", jobRepository)
                .<Report, Report>chunk(10, transactionManager)
                .reader(attendanceLogDbReader.itemReader(transactionManager.getDataSource()))
                .writer(reportWriter.reportItemWriter())
                .taskExecutor(JobConfiguration.taskExecutor())
                .build();
    }
}
