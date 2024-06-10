package one.bca.batch_mini_project.configuration;

import lombok.Data;
import one.bca.batch_mini_project.model.EmployeeAttendance;
import one.bca.batch_mini_project.writers.EmployeeAttendanceWriter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import java.util.List;

@Data
@Configuration
public class EmployeeAttendanceConfiguration {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private DataSourceTransactionManager transactionManager;

    @Autowired
    private EmployeeAttendanceWriter employeeAttendanceWriter;

    @Bean
    public Flow generateReportAndUpdateDB() throws Exception {
        return new FlowBuilder<SimpleFlow>("splitFlow")
                .split(taskExecutor())
                .add(generateReportFlow(), updateDBFlow())
                .build();
    }

    @Bean
    public Flow generateReportFlow() throws Exception {
        return new FlowBuilder<SimpleFlow>("flow1")
                .start(generateReportStep())
                .build();
    }

    @Bean
    public Flow updateDBFlow() throws Exception {
        return new FlowBuilder<SimpleFlow>("flow2")
                .start(updateDBStep())
                .build();
    }

    @Bean
    public Step generateReportStep() throws Exception {
        return new StepBuilder("chunkGenerateReportStep", jobRepository)
                .<EmployeeAttendance, EmployeeAttendance>chunk(10, transactionManager)
                .reader(new ItemReader<EmployeeAttendance>() {
                    @Override
                    public EmployeeAttendance read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                        StepContext stepContext = StepSynchronizationManager.getContext();
                        List<EmployeeAttendance> employeeAttendanceList = (List<EmployeeAttendance>) stepContext.getStepExecution().getJobExecution().getExecutionContext().get("employeeAttendanceList");
                        if (!employeeAttendanceList.isEmpty()) {
                            EmployeeAttendance employeeAttendance = employeeAttendanceList.get(0);
                            employeeAttendanceList.remove(0);
                            return employeeAttendance;
                        }
                        return null;
                    }
                })
                .processor(new ItemProcessor<EmployeeAttendance, EmployeeAttendance>() {
                    @Override
                    public EmployeeAttendance process(EmployeeAttendance item) throws Exception {
                        return item;
                    }
                })
                .writer(employeeAttendanceWriter.employeeAttendanceItemWriter()) // write the employeeAttendanceList contents to dir here
                .build();
    }

    @Bean
    public Step updateDBStep() throws Exception {
        return new StepBuilder("chunkGenerateReportStep", jobRepository)
                .<EmployeeAttendance, EmployeeAttendance>chunk(10, transactionManager)
                .reader(new ItemReader<EmployeeAttendance>() {
                    @Override
                    public EmployeeAttendance read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                        System.out.println("updating db");
                        return null;
                    }
                })
                .writer(new ItemWriter<EmployeeAttendance>() {
                    @Override
                    public void write(Chunk<? extends EmployeeAttendance> chunk) throws Exception {

                    }
                }) // write the employeeAttendanceList contents to dir here
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor(){
        return new SimpleAsyncTaskExecutor("spring_batch");
    }
}
