package one.bca.batch_mini_project.configuration;

import lombok.Data;
import one.bca.batch_mini_project.model.Attendance;
import one.bca.batch_mini_project.model.EmployeeAttendance;
import one.bca.batch_mini_project.objectmapper.AttendanceMapper;
import one.bca.batch_mini_project.readers.AttendanceReader;
import one.bca.batch_mini_project.writers.EmployeeAttendanceWriter;
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
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Configuration
@Data
@Component
public class AttendanceConfiguration {

    private final JobRepository jobRepository;
    private final DataSourceTransactionManager transactionManager;
    private final MessageSource messageSource;
    private final AttendanceReader attendanceReader;
    private final EmployeeAttendanceWriter employeeAttendanceWriter;

    @Bean
    public Step getAttendanceStep(){
        return new StepBuilder("getAttendanceStep", jobRepository)
                .<Attendance, EmployeeAttendance>chunk(10, transactionManager)
                .reader(attendanceReader.csvAttendanceReader())
                .processor(new ItemProcessor<Attendance, EmployeeAttendance>() {
                    @Override
                    public EmployeeAttendance process(Attendance item) throws Exception {
                        EmployeeAttendance employeeAttendance = CalculateWorkingHours(item);
                        System.out.println(employeeAttendance);
                        return employeeAttendance;
                    }
                })
                .writer(employeeAttendanceWriter.employeeAttendanceJDBCWriter(transactionManager.getDataSource()))
                .taskExecutor(JobConfiguration.taskExecutor())
                .build();
    }

    public static EmployeeAttendance CalculateWorkingHours(Attendance item){
        EmployeeAttendance employeeAttendance = new EmployeeAttendance();
        employeeAttendance.setEmployeeId(item.getEmployeeId());
        employeeAttendance.setEmployeeName(item.getEmployeeName());
        employeeAttendance.setAttendanceDate(item.getAttendanceDate());

        // default value
        employeeAttendance.setTotalLeaveDays(0);
        employeeAttendance.setTotalHoursWorked(0);
        employeeAttendance.setTotalOvertimeHoursWorked(0);
        if (item.getLeave()){
            employeeAttendance.setTotalLeaveDays(1);
        }else{
            Duration durHours = Duration.between(item.getClockInTime().toLocalTime(), item.getClockOutTime().toLocalTime());
            if(durHours.toHours() >= 10){
                employeeAttendance.setTotalOvertimeHoursWorked((int)(durHours.toHours() - 8 - 1)); // excess counts starting from the 9th hour
                employeeAttendance.setTotalHoursWorked(8); // add the full working hour since it's the condition
            }else{
                employeeAttendance.setTotalHoursWorked((int) durHours.toHours()); // otherwise just add the delta into it
            }
        }

        return employeeAttendance;
    }
}