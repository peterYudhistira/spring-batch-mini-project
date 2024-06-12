package one.bca.batch_mini_project.writers;

import one.bca.batch_mini_project.model.AttendanceLog;
import one.bca.batch_mini_project.preparedstatement.AttendanceLogPreparedStatementSetter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class AttendanceLogWriter {
    public static String INSERT_ATTENDANCE_LOG_SQL = "insert into "
            + "attendance_log(emp_id, emp_name, attended_date, working_hours, overtime_hours, is_leave)"
            + " values(?,?,?,?,?,?)";

    @Bean
    public ItemWriter<AttendanceLog> itemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<AttendanceLog>()
                .dataSource(dataSource)
                .sql(INSERT_ATTENDANCE_LOG_SQL)
                .itemPreparedStatementSetter(new AttendanceLogPreparedStatementSetter())
                .build();
    }
}
