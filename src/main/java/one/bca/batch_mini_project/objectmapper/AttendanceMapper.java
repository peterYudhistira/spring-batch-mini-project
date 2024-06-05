package one.bca.batch_mini_project.objectmapper;

import one.bca.batch_mini_project.model.Attendance;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class AttendanceMapper implements RowMapper<Attendance> {

    @Override
    public Attendance mapRow(ResultSet rs, int rowNum) throws SQLException {
        Attendance attendance = new Attendance();
        attendance.setAttendanceId(rs.getLong("attendance_id"));
        attendance.setEmployeeId(rs.getLong("emp_id"));
        attendance.setAttendanceDate(rs.getDate("date"));
        attendance.setClockInTime( rs.getTime("clock_in"));
        attendance.setClockOutTime( rs.getTime("clock_out"));
        attendance.setLeave(rs.getBoolean("is_leave"));
        return attendance;
    }
}
