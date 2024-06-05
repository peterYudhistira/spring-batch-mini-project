package one.bca.batch_mini_project.configuration;

import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
public class EmployeeConfiguration {

    private DataSourceTransactionManager dataSourceTransactionManager;

    // select all
    public PagingQueryProvider queryProvider(DataSource dataSource) throws Exception {
        SqlPagingQueryProviderFactoryBean factory = new SqlPagingQueryProviderFactoryBean();
        factory.setSelectClause("SELECT *");
        factory.setFromClause("FROM employee");
        factory.setSortKey("employeeId");
        factory.setDataSource(dataSource);
        return factory.getObject();
    }



}
