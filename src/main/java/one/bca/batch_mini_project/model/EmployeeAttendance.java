package one.bca.batch_mini_project.model;

import lombok.Data;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

// this is the resulting object from the processor layer
@Data
public class EmployeeAttendance implements Serializable {
    private Long employeeAttendanceId; // sepertinya ngga perlu
    private Long employeeId;
    private String employeeName;
    private Integer totalHoursWorked; // aggregate from sum(clockOutTime - clockInTime)
    private Integer totalOvertimeHoursWorked; // aggregate from overtime : anything in excess of 8 hours worked; valid only if 10+ hours worked
    private Integer totalLeaveDays; // aggregate from sum(attendance where isLeave == 1)


}
