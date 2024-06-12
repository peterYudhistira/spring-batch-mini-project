package one.bca.batch_mini_project.model;

import lombok.Data;

@Data
public class Report {
    private Long empId;
    private String empName;
    private int totalWorkingHours;
    private int totalOvertimeHours;
    private int totalLeave;
}
