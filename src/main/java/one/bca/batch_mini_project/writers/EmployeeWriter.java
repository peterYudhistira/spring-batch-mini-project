package one.bca.batch_mini_project.writers;

import one.bca.batch_mini_project.model.Employee;
import one.bca.batch_mini_project.preparedstatement.EmployeePreparedStatementSetter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class EmployeeWriter {
    public static String UPDATE_EMPLOYEE_LOG_SQL = "update employee "
            + "set emp_leave_left = ? "
            + "where emp_id = ?;";

    @Bean
    public ItemWriter<Employee> employeeItemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Employee>()
                .dataSource(dataSource)
                .sql(UPDATE_EMPLOYEE_LOG_SQL)
                .itemPreparedStatementSetter(new EmployeePreparedStatementSetter())
                .build();
    }

}
