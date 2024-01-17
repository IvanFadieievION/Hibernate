package org.taskmanager.taskmanager.service;

import org.taskmanager.taskmanager.domain.Employee;

public interface EmployeeService {
    Employee save(Employee employee);
    Employee findById(Long id);
    Employee update(Employee employee, Long id);
    void deleteById(Long id);
}
