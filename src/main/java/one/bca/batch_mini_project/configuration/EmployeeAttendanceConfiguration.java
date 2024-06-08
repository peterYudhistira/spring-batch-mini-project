package one.bca.batch_mini_project.configuration;

import lombok.Data;
import one.bca.batch_mini_project.model.EmployeeAttendance;
import one.bca.batch_mini_project.writers.EmployeeAttendanceWritter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Data
public class EmployeeAttendanceConfiguration {
    private JobRepository jobRepository;

    private DataSourceTransactionManager transactionManager;

    private EmployeeAttendanceWritter employeeAttendanceWritter;

    @Bean
    public Step generateReportStep() throws Exception {
        return new StepBuilder("chunkGenerateReportStep", jobRepository)
                .<EmployeeAttendance, EmployeeAttendance>chunk(10, transactionManager)
                .reader(new ItemReader<EmployeeAttendance>() {
                    @Override
                    public EmployeeAttendance read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                        return null;
                    }
                })
                .writer(employeeAttendanceWritter.employeeAttendanceItemWriter()) // write the employeeAttendanceList contents to dir here
                .build();
    }
}
