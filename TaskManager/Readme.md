Task manager

Task.class
 String name
 Datetime startTime
 Datetime deadLineTime
 Employee taskLead - oneToOne
 List<Employee> taskEmployees - oneToMany
 ENUM priority
 Category category - oneToOne

Employee.class
 String name
 String surname
 Department department - oneToOne
 boolean isManager
 efficiency(?)

Department.class
 String name
 Integer employeesQuantity


Priority enum
 1 - most important
 2 
 3
 4 - lest important
 
TaskService methods:
 1. DONE change deadLineTime/taskLead/employees/priority
 2. DONE check if deadline is valid (compare deadline and current date/time). Output either false either amount of time left
 3. DONE calculate budget based on employees amount required and priority (1 employee - 1000 budget, 2 - 2000, 1 priority budget x4)
 4. DONE calculate salary for each employee (company - 40%, manager - 20%, others - ?%)
 5. DONE calculate oriented time to solve the task based on employees quantity (1 employee - 24 hours, 2 employee - 12 hours)
 6. DONE calculate is it possible to solve task before deadline based on oriented time (if oriented time < 3x(deadLineTime - startTime)

