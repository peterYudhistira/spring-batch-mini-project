import names
import random
from datetime import datetime, timedelta

# this yields one row of data
def getAttendance(attendanceIndex:int, employee, date, attendanceType):
    resultString = str(attendanceIndex) + "," + str(employee[0]) + "," + employee[1] + "," + date.strftime("%Y-%m-%d")
    if attendanceType == "normal":
        resultString += "," + attendNormal(date)
    elif attendanceType == "undertime":
        resultString += "," + attendUndertime(date)
    elif attendanceType == "overtime":
        resultString += "," + attendOvertime(date)
    else:
        resultString += "," + leave()
    return resultString + "\n"

def attendNormal(date:datetime):
    curTime = (date + timedelta(hours=random.randint(-2, 2), minutes=random.randint(-50,50), seconds=random.randint(-57,57))).time().strftime("%H:%M:%S")
    laterTime = (date + timedelta(hours=random.randint(8,10),minutes=random.randint(0,50), seconds=random.randint(0,57))).time().strftime("%H:%M:%S")
    return curTime +  "," + laterTime + ",0"

def attendOvertime(date:datetime):
    curTime = (date + timedelta(hours=random.randint(-2, 2), minutes=random.randint(-50,50), seconds=random.randint(-57,57))).time().strftime("%H:%M:%S")
    laterTime = (date + timedelta(hours=random.randint(10,16),minutes=random.randint(0,50), seconds=random.randint(0,57))).time().strftime("%H:%M:%S")
    return curTime +  "," + laterTime + ",0"

def attendUndertime(date:datetime):
    curTime = (date + timedelta(hours=random.randint(-2, 2), minutes=random.randint(-50,50), seconds=random.randint(-57,57))).time().strftime("%H:%M:%S")
    laterTime = (date + timedelta(hours=random.randint(3,5),minutes=random.randint(0,50), seconds=random.randint(0,57))).time().strftime("%H:%M:%S")
    return curTime +  "," + laterTime + ",0"

# leavers don't clock in nor out
def leave():
    return "00:00:00,00:00:00,1"


# get 100 different guys + their incremental ID
employeeList = []
for i in range(1, 101):
    employeeList.append((i, names.get_full_name()))

# weighted random
randomAttendance_option = ["normal", "overtime", "undertime", "leave"]
randomAttendance_weight = [0.5, 0.25, 0.15, 0.1]

# make them roll for attendances 30 times this month
attendanceIndex = 1
curDate = datetime(2024, 6, 5, 8, 0, 0)

f = open("attendance_log.csv", "w")
f.write("attendance_id, emp_id,emp_name,date,clock_in,clock_out,is_leave\n")
for i in range(1, 21):
    for employee in employeeList:
        f.write(getAttendance(attendanceIndex, employee, curDate, random.choices(randomAttendance_option, randomAttendance_weight)[0]))
        attendanceIndex += 1
    curDate += timedelta(days=1)
f.close()

f = open("employee_list.sql", "w")
f.write("INSERT INTO employee (emp_name, total_emp_leave, emp_leave_left, total_emp_overtime) VALUES\n")
for employee in employeeList:
    f.write( "('" + employee[1] + "', " + "0, 10, 0" + "),\n")
f.close()