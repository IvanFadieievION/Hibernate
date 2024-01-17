package org.taskmanager.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.taskmanager.taskmanager.domain.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
