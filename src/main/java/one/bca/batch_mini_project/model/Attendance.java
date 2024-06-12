package one.bca.batch_mini_project.model;

import lombok.Data;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

@Data
public class Attendance implements Serializable {
    private Long attendanceId;
    private Long employeeId;
    private String employeeName;
    private Date attendanceDate;
    private Time clockInTime;
    private Time clockOutTime;
    private Boolean leave;
}
