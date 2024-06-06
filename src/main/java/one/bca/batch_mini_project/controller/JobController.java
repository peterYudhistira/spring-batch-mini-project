package one.bca.batch_mini_project.controller;
import one.bca.batch_mini_project.configuration.AttendanceConfiguration;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobController {
    private final JobLauncher jobLauncher;

    private final AttendanceConfiguration attendanceConfiguration;

    public JobController(JobLauncher jobLauncher, AttendanceConfiguration attendanceConfiguration) {
        this.jobLauncher = jobLauncher;
        this.attendanceConfiguration = attendanceConfiguration;
    }

    @GetMapping("/attendance-report")
    public String generateCSVReport(){
        try{
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(attendanceConfiguration.attendanceJob(), jobParameters);
            return "Job Success";
        }catch (Exception e){
            return e.getMessage();
        }
    }
}
