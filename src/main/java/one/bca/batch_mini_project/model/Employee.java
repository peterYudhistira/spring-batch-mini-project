package one.bca.batch_mini_project.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Employee implements Serializable {
    private Long employeeId;
    private String employeeName;

}
