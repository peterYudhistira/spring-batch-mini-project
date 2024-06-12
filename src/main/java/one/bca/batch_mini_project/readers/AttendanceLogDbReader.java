package one.bca.batch_mini_project.readers;

import one.bca.batch_mini_project.model.Report;
import one.bca.batch_mini_project.objectmapper.ReportRowMapper;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class AttendanceLogDbReader {
    @Bean
    public PagingQueryProvider queryProvider(DataSource dataSource) throws Exception {
        SqlPagingQueryProviderFactoryBean factory = new SqlPagingQueryProviderFactoryBean();

        factory.setSelectClause(
                "select " +
                        "emp_id, " +
                        "emp_name, " +
                        "sum(working_hours) as total_working_hours, " +
                        "sum(overtime_hours) as total_overtime_hours, " +
                        "sum(is_leave) as total_leave"
        );
        factory.setFromClause("from attendance_log");
        factory.setSortKey("emp_id");
        factory.setWhereClause(
                "where extract(month from attended_date) = '6' and extract(year from attended_date) = '2024'"
        );
        factory.setGroupClause("group by emp_id, emp_name");
        factory.setDataSource(dataSource);
        return factory.getObject();
    }

    @Bean
    public ItemReader<Report> itemReader(DataSource dataSource) throws Exception {
        return new JdbcPagingItemReaderBuilder<Report>()
                .dataSource(dataSource)
                .name("jdbcCursorItemReader")
                .queryProvider(queryProvider(dataSource))
                .rowMapper(new ReportRowMapper())
                .pageSize(10)
                .build();
    }
}
