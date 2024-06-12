package one.bca.batch_mini_project.processor;

import one.bca.batch_mini_project.model.Employee;
import one.bca.batch_mini_project.model.Report;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class UpdateEmployeeProcessor {
    public ItemProcessor<Report, Employee> updateEmployee() {
        return new ItemProcessor<Report, Employee>() {
            @Override
            public Employee process(Report item) throws Exception {
                Employee employee = new Employee();
                employee.setEmployeeId(item.getEmpId());
                employee.setEmployeeName(item.getEmpName());
                employee.setTotalLeaveLeft(item.getEmpLeaveLeft() - item.getTotalLeave());
                return employee;
            }
        };
    }
}
