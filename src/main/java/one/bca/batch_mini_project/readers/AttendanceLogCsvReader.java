package one.bca.batch_mini_project.readers;

import one.bca.batch_mini_project.model.Attendance;
import one.bca.batch_mini_project.objectmapper.AttendanceMapper;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Component
public class AttendanceLogCsvReader {
    private static String[] tokens = new String[]{
            "attendance_id",
            "emp_id",
            "emp_name",
            "date",
            "clock_in",
            "clock_out",
            "is_leave"
    };

    @Bean
    public ItemReader<Attendance> csvAttendanceReader() {
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
}
