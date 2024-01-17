package org.taskmanager.taskmanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.taskmanager.taskmanager.domain.Department;
import org.taskmanager.taskmanager.service.DepartmentService;

@RestController
@RequestMapping("/department")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @PostMapping
    public Department create(@RequestBody Department department) {
       return departmentService.create(department);
    }

    @GetMapping("/{id}")
    public Department getById(@PathVariable Long id) {
        return departmentService.findById(id);
    }

    @PutMapping("/{id}")
    public Department update(@PathVariable Long id,
                             @RequestBody Department department) {
        return departmentService.update(id, department);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        departmentService.deleteById(id);
    }
}
