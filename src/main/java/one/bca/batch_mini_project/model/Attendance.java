package one.bca.batch_mini_project.model;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Data // this annotation gives auto setter-getter
public class Attendance implements Serializable {
    private Long attendanceId;
    private String employeeId;
    private String employeeName;
    private Date attendanceDate;
    private Timestamp clockInTime;
    private Timestamp clockOutTime;
    private boolean isLeave;
    private int overtimeMinutes;

}
