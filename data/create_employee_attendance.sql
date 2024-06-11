drop table if exists employee_attendance;
create table if not exists employee_attendance(
    emp_attendance_id SERIAL,
    emp_id INT,
    attendance_date DATE NOT NULL,
    emp_name VARCHAR(20),
    total_hours_worked INT,
    total_overtime_hours_worked INT,
    total_leave_days INT,
    PRIMARY KEY(emp_attendance_id)
);