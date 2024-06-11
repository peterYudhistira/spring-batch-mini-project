package one.bca.batch_mini_project.objectmapper;

import one.bca.batch_mini_project.model.EmployeeAttendance;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EmployeeAttendanceStatementSetter implements ItemPreparedStatementSetter<EmployeeAttendance> {
    @Override
    public void setValues(EmployeeAttendance item, PreparedStatement ps) throws SQLException {
        ps.setLong(1, item.getEmployeeId());
        ps.setString(2, item.getEmployeeName());
        ps.setDate(3, new java.sql.Date(item.getAttendanceDate().getTime()));
        ps.setInt(4, item.getTotalHoursWorked());
        ps.setInt(5, item.getTotalOvertimeHoursWorked());
        ps.setInt(6, item.getTotalLeaveDays());
    }
}
