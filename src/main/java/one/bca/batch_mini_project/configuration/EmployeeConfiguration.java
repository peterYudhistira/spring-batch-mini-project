package one.bca.batch_mini_project.configuration;

import lombok.Data;
import one.bca.batch_mini_project.model.Employee;
import one.bca.batch_mini_project.readers.EmployeeReader;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
public class EmployeeConfiguration {

    private final JobRepository jobRepository;
    private final DataSourceTransactionManager transactionManager;
    private final EmployeeReader employeeReader;

    public Step getEmployeeStep() throws Exception {
        return new StepBuilder("getEmployeeStep", jobRepository)
                .<Employee, Employee>chunk(10, transactionManager)
                .reader(employeeReader.employeeReader(transactionManager.getDataSource()))
                .processor(new ItemProcessor<Employee, Employee>() {
                    @Override
                    public Employee process(Employee employee) throws Exception {
                        StepContext stepContext = StepSynchronizationManager.getContext();
                        List<Employee> processedItems = (List<Employee>) stepContext.getStepExecution().getJobExecution().getExecutionContext().get("employeeList");

                        if(processedItems == null) {
                            processedItems = new ArrayList<>();
                            processedItems.add(employee);
                        }else {
                            processedItems.add(employee);
                        }
                        stepContext.getStepExecution().getJobExecution().getExecutionContext().put("employeeList", processedItems);
                        return employee;
                    }
                })
                .writer(new ItemWriter<Employee>(){
                    @Override
                    public void write(Chunk<? extends Employee> chunk) throws Exception {
                    }
                })
                .build();

    }

}
