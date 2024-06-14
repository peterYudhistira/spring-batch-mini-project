select
	em.emp_id,
	em.emp_name,
	sum(working_hours) as total_working_hours,
	sum(overtime_hours) as total_overtime_hours,
	sum(is_leave) as total_leave,
	(count(em.emp_id) - sum(al.is_leave)) as total_attendance,
	em.emp_leave_left
from attendance_log al
join employee em on al.emp_id = em.emp_id
where extract(month from attended_date) = '6'
group by em.emp_id, em.emp_name
order by emp_id asc;

select
	emp_id,
	emp_name,
	sum(working_hours) as total_working_hours,
	sum(overtime_hours) as total_overtime_hours,
	sum(is_leave) as total_leave,
	(count(emp_id) - sum(is_leave)) as total_attendance
from attendance_log
where extract(month from attended_date) = '6' and extract(year from attended_date) = '2024'
group by emp_id, emp_name;
