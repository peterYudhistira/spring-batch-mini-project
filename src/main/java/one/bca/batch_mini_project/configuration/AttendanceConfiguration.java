package one.bca.batch_mini_project.configuration;

import lombok.Data;
import one.bca.batch_mini_project.model.Attendance;
import one.bca.batch_mini_project.model.EmployeeAttendance;
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
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import java.time.Duration;
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
    private final MessageSource messageSource;

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
                        List<EmployeeAttendance> employeeAttendanceList = (List<EmployeeAttendance>) stepContext.getStepExecution().getJobExecution().getExecutionContext().get("employeeAttendanceList");
                        for (EmployeeAttendance employeeAttendance : employeeAttendanceList) {
                            if(employeeAttendance.getEmployeeId() == item.getEmployeeId()){
                                System.out.println(item.getEmployeeName() + " " + Thread.currentThread().getName());
                                CalculateWorkingHours(item, employeeAttendance);
                            }
                        }

                        return item;
                    }
                })
                .writer(new ItemWriter<Attendance>() {
                    @Override
                    public void write(Chunk<? extends Attendance> chunk) throws Exception {

                    }
                })
                .taskExecutor(JobConfiguration.taskExecutor())
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
    public static void CalculateWorkingHours(Attendance item, EmployeeAttendance employeeAttendance){
        if (item.getLeave()){
            // if leave, increment leave only
            employeeAttendance.setTotalLeaveDays(employeeAttendance.getTotalLeaveDays() + 1);
        }else {
            Duration durHours = Duration.between(item.getClockInTime().toLocalTime(), item.getClockOutTime().toLocalTime());
            if(durHours.toHours() >= 10){
                // if at least 10 hours, the excess starting from the 9th hour is counted as overtime
                employeeAttendance.setTotalOvertimeHoursWorked(employeeAttendance.getTotalOvertimeHoursWorked() + (int) (durHours.toHours() - 8 - 1));
                // also add 8
                employeeAttendance.setTotalHoursWorked(employeeAttendance.getTotalHoursWorked() + 8);
            }else{
                // otherwise just add the delta into it
                employeeAttendance.setTotalHoursWorked(employeeAttendance.getTotalHoursWorked() + (int) durHours.toHours());
            }
        }
    }
}