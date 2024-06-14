package one.bca.batch_mini_project;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBatchTest
@SpringBootTest
class StepTests {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    private JobExecution jobExecution;

    @BeforeEach
    public void setUp(@Autowired Job job) {
        this.jobLauncherTestUtils.setJob(job);
    }

    @ParameterizedTest
    @ValueSource(strings = {"calculateWorkingHoursStep", "updateEmployeeStep", "generateReportStep"})
    void testStepRanSuccessfully(String stepName) {
        jobExecution = this.jobLauncherTestUtils.launchStep(stepName);
        Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
    }

    @AfterEach
    public void tearDown() {
        this.jobRepositoryTestUtils.removeJobExecution(jobExecution);
    }

}
