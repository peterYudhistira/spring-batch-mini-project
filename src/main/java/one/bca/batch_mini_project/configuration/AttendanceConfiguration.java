package one.bca.batch_mini_project.configuration;

import lombok.Data;
import one.bca.batch_mini_project.model.Attendance;
import one.bca.batch_mini_project.model.Employee;
import one.bca.batch_mini_project.objectmapper.AttendanceMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import java.util.List;

@Configuration
@Data
public class AttendanceConfiguration {

    private static String[] tokens = new String[]{
            "attendance_id",
            "emp_id",
            "emp_name",
            "date",
            "clock_in",
            "clock_out",
            "is_leave"
    };

    private final JobRepository jobRepository;
    private final DataSourceTransactionManager transactionManager;

    public ItemReader<Attendance> csvAttendanceReader(){
        FlatFileItemReader<Attendance> reader = new FlatFileItemReader<>();
        reader.setLinesToSkip(1);
        reader.setResource(new FileSystemResource("data/attendance_log.csv"));
        DefaultLineMapper<Attendance> lineMapper = new DefaultLineMapper<Attendance>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(",");
        tokenizer.setNames(tokens);
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new AttendanceMapper());

        reader.setLineMapper(lineMapper);
        reader.open(new ExecutionContext());
        return reader;
    }

    @Bean
    public Step getAttendanceStep(){
        return new StepBuilder("getAttendanceStep", jobRepository)
                .<Attendance, Attendance>chunk(10, transactionManager)
                .reader(csvAttendanceReader())
                .processor(new ItemProcessor<Attendance, Attendance>() {

                    @Override
                    public Attendance process(Attendance item) throws Exception {
                        // pull out the Employee item here
                        StepContext stepContext = StepSynchronizationManager.getContext();
                        List<Employee> employeeList = (List<Employee>) stepContext.getStepExecution().getJobExecution().getExecutionContext().get("employeeList");

                        for (Employee employee : employeeList) {
                            if(employee.getEmployeeId() == item.getEmployeeId()){
                                System.out.println("Jackpot! " + item.getEmployeeName());
                            }
                        }
                        // then put it to context
                        return item;
                    }
                })
                .writer(new ItemWriter<Attendance>() {

                    @Override
                    public void write(Chunk<? extends Attendance> chunk) throws Exception {
                        
                    }
                })
                .build();
    }

    @Bean
    public Job attendanceJob(){
        // sementara aja, harusnya bukan panggil job ini
        return new JobBuilder("ReportingJob", jobRepository)
                .start(getAttendanceStep())
                .build();
    }

    // put functions that help the processor here
}
