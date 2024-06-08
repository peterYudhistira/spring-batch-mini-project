package one.bca.batch_mini_project.controller;
import lombok.Data;
import one.bca.batch_mini_project.configuration.AttendanceConfiguration;
import one.bca.batch_mini_project.configuration.JobConfiguration;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Data
@RestController
public class JobController {
    private final JobLauncher jobLauncher;
    private final JobConfiguration jobConfiguration;

    @GetMapping("/attendance-report")
    public String generateCSVReport(){
        try{
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(jobConfiguration.employeeAttendanceJob(), jobParameters);
            return "Job Success";
        }catch (Exception e){
            return e.getMessage();
        }
    }
}
