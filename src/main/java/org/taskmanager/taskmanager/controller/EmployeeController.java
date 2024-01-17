package org.taskmanager.taskmanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.taskmanager.taskmanager.domain.Employee;
import org.taskmanager.taskmanager.service.EmployeeService;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/{id}")
    public Employee getById(@PathVariable Long id) {
        return employeeService.findById(id);
    }

    @PostMapping
    public Employee create(@RequestBody Employee employee) {
        return employeeService.save(employee);
    }

    @PutMapping("/{id}")
    public Employee update(@PathVariable Long id,
                           @RequestBody Employee employee) {
        return employeeService.update(employee, id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        employeeService.deleteById(id);
    }
}
