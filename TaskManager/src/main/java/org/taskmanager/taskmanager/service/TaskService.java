package org.taskmanager.taskmanager.service;

import org.taskmanager.taskmanager.domain.Employee;
import org.taskmanager.taskmanager.domain.Task;
import org.taskmanager.taskmanager.enums.Priority;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface TaskService {
    Task createNewTask(Task task);
    Task findById(Long id);
    Task update(Long id, Task task);
    void deleteById(Long id);
    Task setNewDeadLineTime(Long taskId, LocalDateTime newDeadLineTime);
    Task setNewTaskEmployees(Long taskId, List<Employee> employees);
    Employee addNewEmployeeToTask(Long taskId, Long employeeId);
    void setTaskPriority(Long taskId, Priority priority);
    Long calculateTaskTime(Long taskId);
    String calculateIsItEnoughTime(Long taskId);
    String calculateIfDeadLineIsNotOverDue(Long taskId);
    int calculateTaskBudget(Long taskId);
    Map<String, Double> calculateSalaryDistribution (Long taskId);
}
