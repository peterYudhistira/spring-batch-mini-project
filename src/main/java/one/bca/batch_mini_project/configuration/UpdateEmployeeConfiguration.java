package one.bca.batch_mini_project.configuration;

import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
public class UpdateEmployeeConfiguration {
    private final JobRepository jobRepository;
    private final DataSourceTransactionManager transactionManager;

    public UpdateEmployeeConfiguration(
            JobRepository jobRepository,
            DataSourceTransactionManager transactionManager
    ) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }
}
