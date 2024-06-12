package one.bca.batch_mini_project.preparedstatement;

import one.bca.batch_mini_project.model.AttendanceLog;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AttendanceLogPreparedStatementSetter implements ItemPreparedStatementSetter<AttendanceLog> {

	@Override
	public void setValues(AttendanceLog item, PreparedStatement ps) throws SQLException {
		ps.setLong(1, item.getEmpId());
		ps.setString(2, item.getEmpName());
		ps.setDate(3, new Date(item.getAttendedDate().getTime()));
		ps.setInt(4,item.getWorkingHours());
		ps.setInt(5, item.getOvertimeHours());
		ps.setInt(6, item.getIsLeave());
	}

}
