package one.bca.batch_mini_project.model;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;


// this object stores the value yielded by one employee's Attendance.
@Data
public class EmployeeAttendance implements Serializable {
//    private Long employeeAttendanceId; // sepertinya ngga perlu
    private Long employeeId;
    private String employeeName;
    private Date attendanceDate;
    private Integer totalHoursWorked; // calculate from sum(clockOutTime - clockInTime)
    private Integer totalOvertimeHoursWorked; // calculate from overtime : anything in excess of 8 hours worked; valid only if 10+ hours worked
    private Integer totalLeaveDays; // 1 if leaving today, 0 if not. baffling, i know.
}
