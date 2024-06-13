package one.bca.batch_mini_project.readers;

import one.bca.batch_mini_project.model.Report;
import one.bca.batch_mini_project.objectmapper.ReportRowMapper;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.text.SimpleDateFormat;

@Configuration
public class AttendanceLogDbReader {
    public PagingQueryProvider queryProvider(DataSource dataSource) throws Exception {
        StepContext stepContext = StepSynchronizationManager.getContext();
        long time = stepContext.getStepExecution().getJobExecution().getJobParameters().getLong("time");
        SqlPagingQueryProviderFactoryBean factory = new SqlPagingQueryProviderFactoryBean();
        factory.setSelectClause(
                "select " +
                        "em.emp_id as emp_id, " +
                        "em.emp_name as emp_name, " +
                        "sum(working_hours) as total_working_hours, " +
                        "sum(overtime_hours) as total_overtime_hours, " +
                        "sum(is_leave) as total_leave, " +
                        "(count(em.emp_id) - sum(al.is_leave)) as total_attendance, " +
                        "em.emp_leave_left as emp_leave_left "
        );
        factory.setFromClause(
                "from attendance_log al " +
                "join employee em on al.emp_id = em.emp_id"
        );
        factory.setSortKey("emp_id");
        factory.setWhereClause(
                "where extract(month from attended_date) = '" + new SimpleDateFormat("M").format(time) +"' and extract(year from attended_date) = '" + new SimpleDateFormat("yyyy").format(time) + "'"
        );
        factory.setGroupClause("group by em.emp_id, em.emp_name");
        factory.setDataSource(dataSource);
        return factory.getObject();
    }

    @Bean
    @StepScope
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
