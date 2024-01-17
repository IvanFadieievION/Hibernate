package org.taskmanager.taskmanager.service;

import org.taskmanager.taskmanager.domain.Department;

public interface DepartmentService {
    Department create(Department department);
    Department findById(Long id);
    Department update(Long id, Department department);
    void deleteById(Long id);
}
