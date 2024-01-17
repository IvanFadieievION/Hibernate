package org.taskmanager.taskmanager.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.taskmanager.taskmanager.domain.Department;
import org.taskmanager.taskmanager.exception.DepartmentNotFoundException;
import org.taskmanager.taskmanager.repository.DepartmentRepository;
import org.taskmanager.taskmanager.service.DepartmentService;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    @Override
    public Department create(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    public Department findById(Long id) {
        return departmentRepository.findById(id).orElseThrow(()
                -> new DepartmentNotFoundException("Department by provided id " + id
                + " doesn't exists."));
    }

    @Override
    public Department update(Long id, Department department) {
        return departmentRepository.save(department.setId(id));
    }

    @Override
    public void deleteById(Long id) {
        departmentRepository.deleteById(id);
    }
}
