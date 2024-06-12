package one.bca.batch_mini_project.model;
import lombok.Data;
import java.util.Date;

@Data
public class AttendanceLog {
    private Long empId;
    private String empName;
    private Date attendedDate;
    private Integer workingHours;
    private Integer overtimeHours;
    private Integer isLeave;
}
