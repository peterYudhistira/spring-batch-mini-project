package one.bca.batch_mini_project.writers;

import one.bca.batch_mini_project.model.EmployeeAttendance;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Component
public class EmployeeAttendanceWriter {
    public static String[] names = new String[] { "employeeId", "employeeName", "totalHoursWorked", "totalOvertimeHoursWorked", "totalLeaveDays" };

    @Bean
    public ItemWriter<EmployeeAttendance> employeeAttendanceItemWriter(){
        FlatFileItemWriter<EmployeeAttendance> itemWriter = new FlatFileItemWriter<>();

        Chunk<? extends EmployeeAttendance> data = new Chunk<>();

        itemWriter.setResource(new FileSystemResource("data/Employee_attendance_report_"+ new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()) +".csv"));
        itemWriter.setHeaderCallback(writer -> {
            writer.write("employeeId,employeeName,totalHoursWorked,totalOvertimeHoursWorked,totalLeaveDays");
        });

        DelimitedLineAggregator<EmployeeAttendance> aggregator = new DelimitedLineAggregator<EmployeeAttendance>();
        aggregator.setDelimiter(",");

        BeanWrapperFieldExtractor<EmployeeAttendance> fieldExtractor = new BeanWrapperFieldExtractor<EmployeeAttendance>();
        fieldExtractor.setNames(names);
        aggregator.setFieldExtractor(fieldExtractor);

        itemWriter.setLineAggregator(aggregator);

        return itemWriter;
    }
}