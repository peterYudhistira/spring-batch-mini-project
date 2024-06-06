package one.bca.batch_mini_project.configuration;

import one.bca.batch_mini_project.model.EmployeeAttendance;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;

public class EmployeeAttendanceWritter {
    public static String[] names = new String[] { "employeeId", "employeeName", "totalHoursWorked", "totalOvertimeHoursWorked", "totalLeaveDays" };

    @Bean
    public ItemWriter<EmployeeAttendance> employeeAttendanceItemWriter(){
        FlatFileItemWriter<EmployeeAttendance> itemWriter = new FlatFileItemWriter<>();

        Chunk<? extends EmployeeAttendance> data = new Chunk<>();

        itemWriter.setResource(new FileSystemResource("data/shipped_orders_output2.csv"));

        DelimitedLineAggregator<EmployeeAttendance> aggregator = new DelimitedLineAggregator<EmployeeAttendance>();
        aggregator.setDelimiter(",");

        BeanWrapperFieldExtractor<EmployeeAttendance> fieldExtractor = new BeanWrapperFieldExtractor<EmployeeAttendance>();
        fieldExtractor.setNames(names);
        aggregator.setFieldExtractor(fieldExtractor);

        itemWriter.setLineAggregator(aggregator);

        return itemWriter;
    }
}
