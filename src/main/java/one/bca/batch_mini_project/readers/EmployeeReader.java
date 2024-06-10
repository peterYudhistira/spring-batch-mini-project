package one.bca.batch_mini_project.readers;

import lombok.Data;
import one.bca.batch_mini_project.model.Employee;
import one.bca.batch_mini_project.objectmapper.EmployeeMapper;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Data
@Component
public class EmployeeReader {

    public static String GET_EMPLOYEE_SQL = "SELECT emp_id, emp_name, total_emp_leave, total_emp_overtime FROM employee;";


    public ItemReader<Employee> employeeReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Employee>()
                .dataSource(dataSource)
                .name("employeeReader")
                .sql(GET_EMPLOYEE_SQL)
                .rowMapper(new EmployeeMapper())
                .build();
    }
}
