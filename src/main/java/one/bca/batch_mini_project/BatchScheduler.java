package one.bca.batch_mini_project;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
public class BatchScheduler {

    @Scheduled(fixedRate = 5000)
    public void runJob(){
        System.out.println("Running Batch Job! test push");
    }
}
