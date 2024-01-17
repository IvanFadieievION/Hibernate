package org.taskmanager.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.taskmanager.taskmanager.domain.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
