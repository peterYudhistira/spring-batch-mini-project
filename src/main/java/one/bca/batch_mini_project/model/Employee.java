package one.bca.batch_mini_project.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Employee implements Serializable {
    private Long employeeId;
    private String employeeName;
    private int yearlyLeaveTotal; // jumlah cuti total
    private int overtimeMinutes; // durasi  lembur dalam menit. menurutku ini bisa dihapus aja, digantikan dengan hasil processing data?

}
