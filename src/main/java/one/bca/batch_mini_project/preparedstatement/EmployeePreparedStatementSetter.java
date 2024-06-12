package one.bca.batch_mini_project.preparedstatement;

import one.bca.batch_mini_project.model.Employee;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EmployeePreparedStatementSetter implements ItemPreparedStatementSetter<Employee> {

	@Override
	public void setValues(Employee item, PreparedStatement ps) throws SQLException {
		ps.setInt(1, item.getTotalLeaveLeft());
		ps.setLong(2, item.getEmployeeId());
	}

}
