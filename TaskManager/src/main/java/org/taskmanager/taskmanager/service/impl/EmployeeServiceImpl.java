package org.taskmanager.taskmanager.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.taskmanager.taskmanager.domain.Employee;
import org.taskmanager.taskmanager.exception.EmployeeNotFoundException;
import org.taskmanager.taskmanager.repository.EmployeeRepository;
import org.taskmanager.taskmanager.service.DepartmentService;
import org.taskmanager.taskmanager.service.EmployeeService;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentService departmentService;
    @Override
    public Employee save(Employee employee) {
        return employeeRepository.save(employee
                .setDepartment(departmentService
                        .findById(employee.getDepartment().getId())));
    }

    @Override
    public Employee findById(Long id) {
        return employeeRepository.findById(id).orElseThrow(()
                -> new EmployeeNotFoundException("Employee by provided id " + id
                + " doesn't exists."));
    }

    @Override
    public Employee update(Employee employee, Long id) {
        return employeeRepository.save(employee.setId(id));
    }

    @Override
    public void deleteById(Long id) {
        employeeRepository.deleteById(id);
    }
}
