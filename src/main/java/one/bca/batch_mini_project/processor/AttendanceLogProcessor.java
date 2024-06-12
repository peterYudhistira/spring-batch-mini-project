package one.bca.batch_mini_project.processor;

import one.bca.batch_mini_project.model.Attendance;
import one.bca.batch_mini_project.model.AttendanceLog;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class AttendanceLogProcessor {
    public ItemProcessor<Attendance, AttendanceLog> processAttendance() {
        return new ItemProcessor<Attendance, AttendanceLog>() {
            @Override
            public AttendanceLog process(Attendance item) throws Exception {
                AttendanceLog attendanceLog = new AttendanceLog();
                attendanceLog.setEmpId(item.getEmployeeId());
                attendanceLog.setEmpName(item.getEmployeeName());
                attendanceLog.setAttendedDate(item.getAttendanceDate());

                if (Boolean.TRUE.equals(item.getLeave())) {
                    attendanceLog.setIsLeave(1);
                    attendanceLog.setWorkingHours(0);
                    attendanceLog.setOvertimeHours(0);
                } else {
                    attendanceLog.setIsLeave(0);

                    Duration durationHours = Duration.between(item.getClockInTime().toLocalTime(), item.getClockOutTime().toLocalTime());
                    if (durationHours.toHours() >= 10) {
                        attendanceLog.setWorkingHours(8);
                        attendanceLog.setOvertimeHours((int) (durationHours.toHours() - 8 - 1));
                    } else {
                        attendanceLog.setWorkingHours((int) durationHours.toHours());
                        attendanceLog.setOvertimeHours(0);
                    }
                }
                return attendanceLog;
            }
        };
    }
}
