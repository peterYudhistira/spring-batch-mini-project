drop table if exists employee;
create table if not exists employee(
    emp_id SERIAL,
    emp_name VARCHAR(20),
    total_emp_leave INT,
    emp_leave_left INT,
    total_emp_overtime INT,
    PRIMARY KEY(emp_id) );