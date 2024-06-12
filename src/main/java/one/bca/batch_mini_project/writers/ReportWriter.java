package one.bca.batch_mini_project.writers;
import one.bca.batch_mini_project.model.Report;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Component
public class ReportWriter {
    public static String[] names = new String[] { "empId", "empName", "totalWorkingHours", "totalOvertimeHours", "totalLeave", "totalAttendance", "empLeaveLeft" };

    @Bean
    public ItemWriter<Report> reportItemWriter(){
        FlatFileItemWriter<Report> itemWriter = new FlatFileItemWriter<>();

        Chunk<? extends Report> data = new Chunk<>();

        itemWriter.setResource(new FileSystemResource("data/Employee_attendance_report_"+ new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()) +".csv"));
        itemWriter.setHeaderCallback(writer -> {
            writer.write("empId,empName,totalWorkingHours,totalOvertimeHours,totalLeave,totalAttendance,empLeaveLeft");
        });

        DelimitedLineAggregator<Report> aggregator = new DelimitedLineAggregator<Report>();
        aggregator.setDelimiter(",");

        BeanWrapperFieldExtractor<Report> fieldExtractor = new BeanWrapperFieldExtractor<Report>();
        fieldExtractor.setNames(names);
        aggregator.setFieldExtractor(fieldExtractor);

        itemWriter.setLineAggregator(aggregator);

        return itemWriter;
    }
}
