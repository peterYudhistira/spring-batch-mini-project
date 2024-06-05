package one.bca.batch_mini_project.objectmapper;

import one.bca.batch_mini_project.model.Employee;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EmployeeStatementSetter implements ItemPreparedStatementSetter<Employee> {
    @Override
    public void setValues(Employee employee, PreparedStatement ps) throws SQLException {
        // employeeId tidak dimasukkan karena autoincrement
        ps.setString(1, employee.getEmployeeName());
        ps.setInt(2, employee.getTotalEmployeeLeave());
        ps.setInt(3, employee.getTotalLeaveLeft());
        ps.setInt(4, employee.getTotalEmployeeOvertime());
    }
}
