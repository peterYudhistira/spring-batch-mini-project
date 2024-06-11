package one.bca.batch_mini_project.objectmapper;

import one.bca.batch_mini_project.model.Attendance;
import one.bca.batch_mini_project.model.EmployeeAttendance;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class EmployeeAttendanceMapper implements FieldSetMapper<EmployeeAttendance> {
    @Override
    public EmployeeAttendance mapFieldSet(FieldSet fieldSet) throws BindException {
        EmployeeAttendance employeeAttendance = new EmployeeAttendance();
        // map here

        return employeeAttendance;
    }
}
