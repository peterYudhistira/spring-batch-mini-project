drop table if exists attendance_log;
create table if not exists attendance_log(
    attendance_id serial,
    emp_id int,
    emp_name varchar(20),
    attended_date date,
    working_hours int,
    overtime_hours int,
    is_leave int,
    primary key(attendance_id)
    );