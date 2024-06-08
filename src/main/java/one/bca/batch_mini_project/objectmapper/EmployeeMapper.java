package one.bca.batch_mini_project.objectmapper;

import one.bca.batch_mini_project.model.Employee;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeMapper implements RowMapper<Employee> {
    @Override
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
        Employee employee = new Employee();
        employee.setEmployeeId(rs.getLong("emp_id"));
        employee.setEmployeeName(rs.getString("emp_name"));
        employee.setTotalEmployeeLeave(rs.getInt("total_emp_leave"));
        employee.setTotalEmployeeOvertime(rs.getInt("total_emp_overtime"));
        return employee;
    }
}
