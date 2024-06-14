package one.bca.batch_mini_project.configuration;

import one.bca.batch_mini_project.listener.RetryLogListener;
import one.bca.batch_mini_project.model.Employee;
import one.bca.batch_mini_project.model.Report;
import one.bca.batch_mini_project.processor.UpdateEmployeeProcessor;
import one.bca.batch_mini_project.readers.AttendanceLogDbReader;
import one.bca.batch_mini_project.writers.EmployeeWriter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
public class UpdateEmployeeConfiguration {
    private final JobRepository jobRepository;
    private final DataSourceTransactionManager transactionManager;
    private final AttendanceLogDbReader attendanceLogDbReader;
    private final UpdateEmployeeProcessor updateEmployeeProcessor;
    private final EmployeeWriter employeeWriter;

    public UpdateEmployeeConfiguration(
            JobRepository jobRepository,
            DataSourceTransactionManager transactionManager,
            AttendanceLogDbReader attendanceLogDbReader,
            UpdateEmployeeProcessor updateEmployeeProcessor,
            EmployeeWriter employeeWriter
    ) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.attendanceLogDbReader = attendanceLogDbReader;
        this.updateEmployeeProcessor = updateEmployeeProcessor;
        this.employeeWriter = employeeWriter;
    }

    @Bean
    public Step updateEmployeeStep() throws Exception {
        return new StepBuilder("updateEmployeeStep", jobRepository)
                .<Report, Employee>chunk(10, transactionManager)
                .reader(attendanceLogDbReader.itemReader(transactionManager.getDataSource()))
                .processor(updateEmployeeProcessor.updateEmployee())
                .writer(employeeWriter.employeeItemWriter(transactionManager.getDataSource()))
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(5)
                .listener(new RetryLogListener())
                .taskExecutor(JobConfiguration.taskExecutor())
                .build();
    }
}
