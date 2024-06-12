package one.bca.batch_mini_project.objectmapper;

import one.bca.batch_mini_project.model.Report;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportRowMapper implements RowMapper<Report> {

    @Override
    public Report mapRow(ResultSet rs, int rowNum) throws SQLException {
        Report report = new Report();
        report.setEmpId(rs.getLong("emp_id"));
        report.setEmpName(rs.getString("emp_name"));
        report.setTotalWorkingHours(rs.getInt("total_working_hours"));
        report.setTotalOvertimeHours(rs.getInt("total_overtime_hours"));
        report.setTotalLeave(rs.getInt("total_leave"));
        return report;
    }

}
