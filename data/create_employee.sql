drop table employee;
create table if not exists employee(
    emp_id SERIAL,
    emp_name VARCHAR(20),
    emp_leave_left INT,
    PRIMARY KEY(emp_id)
);
