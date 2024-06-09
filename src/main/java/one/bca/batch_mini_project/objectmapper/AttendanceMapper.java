package one.bca.batch_mini_project.objectmapper;

import one.bca.batch_mini_project.model.Attendance;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;
import java.sql.Time;

public class AttendanceMapper implements FieldSetMapper<Attendance> {
    @Override
    public Attendance mapFieldSet(FieldSet rs) throws BindException{
        Attendance attendance = new Attendance();
        attendance.setAttendanceId(rs.readLong("attendance_id"));
        attendance.setEmployeeId(rs.readLong("emp_id"));
        attendance.setEmployeeName(rs.readString("emp_name"));
        attendance.setAttendanceDate(rs.readDate("date")); // readDate second parameter is pattern.
        attendance.setClockInTime(Time.valueOf(rs.readString("clock_in")) ); // time is kinda finicky?
        attendance.setClockOutTime(Time.valueOf(rs.readString("clock_out")));
        attendance.setLeave(rs.readInt("is_leave") == 1);
        return attendance;
    }
}
