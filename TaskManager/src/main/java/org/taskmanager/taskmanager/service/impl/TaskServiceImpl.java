package org.taskmanager.taskmanager.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.taskmanager.taskmanager.domain.Employee;
import org.taskmanager.taskmanager.domain.Task;
import org.taskmanager.taskmanager.enums.Priority;
import org.taskmanager.taskmanager.exception.*;
import org.taskmanager.taskmanager.repository.TaskRepository;
import org.taskmanager.taskmanager.service.EmployeeService;
import org.taskmanager.taskmanager.service.TaskService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final EmployeeService employeeService;
    @Override
    public Task createNewTask(Task task) {
        if (task.getName() == null || task.getStartTime() == null || task.getDeadLineTime() == null) {
            throw new TaskCreationException("During task creation it's compulsory " +
                    "to fill: Name, Start time, Deadline time.");
        }
        List<Employee> taskEmployees = Optional.ofNullable(task.getTaskEmployees())
                .orElse(Collections.emptyList())
                .stream()
                .map(employeeRequest -> employeeService.findById(employeeRequest.getId()))
                .collect(Collectors.toList());

        task.setTaskEmployees(taskEmployees);
        return taskRepository.save(task);
    }

    @Override
    public Task findById(Long id) {
        return taskRepository.findById(id).orElseThrow(()
                -> new TaskNotFoundException("Task was not found"));
    }

    @Override
    public Task update(Long id, Task task) {
        Task updatedTask = task.setId(id);
        return taskRepository.save(updatedTask);
    }

    @Override
    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public Task setNewDeadLineTime(Long id, LocalDateTime newDeadLineTime) {
        Task task = findById(id);
        if (task.getDeadLineTime().isAfter(newDeadLineTime)) {
            throw new InvalidDeadlineException("New deadline time incorrect, it cannot " +
                    "be earlier than the previous");
        }
        return taskRepository.save(task.setDeadLineTime(newDeadLineTime));
    }

    @Override
    public Task setNewTaskEmployees(Long id, List<Employee> employees) {
        Task task = findById(id);
        List<Employee> employeeList = employees.stream()
                .map(employee -> employeeService.findById(employee.getId()))
                .collect(Collectors.toList());
        task.setTaskEmployees(employeeList);
        return taskRepository.save(task.setTaskEmployees(employeeList));
    }

    @Override
    public Employee addNewEmployeeToTask(Long id, Long employeeId) {
        Task task = findById(id);
        Employee employee = employeeService.findById(employeeId);
        if (task.getTaskEmployees().contains(employee)) {
            throw new EmployeeAlreadyAssignedException("This employee is already set for this task.");
        }
        task.getTaskEmployees().add(employee);
        taskRepository.save(task);
        return employee;
    }

    @Override
    public void setTaskPriority(Long id, Priority priority) {
        Task task = findById(id);
        if (task.getPriority() == priority) {
            throw new PriorityValidationException("Input priority level has to be different");
        }
        taskRepository.save(task.setPriority(priority));
    }

    @Override
    public Long calculateTaskTime(Long id) {
        Task task = findById(id);
        if (task.getTaskEmployees().isEmpty()) {
            throw new NoEmployeesAssignedException(
                    "It is impossible to calculate approximate time " +
                    "for task completion due to employees absence.");
        }
        long hoursPerEmployee = 24L;
        int numberOfEmployees = task.getTaskEmployees().size();
        return hoursPerEmployee/numberOfEmployees;
    }

    @Override
    public String calculateIsItEnoughTime(Long id) {
        Task task = findById(id);
        LocalDateTime deadLineTime = task.getDeadLineTime();
        Long orientedTaskTime = calculateTaskTime(task.getId());
        LocalDateTime orientedFinishTime = LocalDateTime.now().plusHours(orientedTaskTime);
        return orientedFinishTime.isBefore(deadLineTime)
                ? "There is enough time to complete the task." : "Not enough time to complete the task.";
    }

    @Override
    public String calculateIfDeadLineIsNotOverDue(Long id) {
        Task task = findById(id);
        if (task.getDeadLineTime().isBefore(LocalDateTime.now())) {
            Duration overdueDuration = Duration.between(task.getDeadLineTime(), LocalDateTime.now());
            throw new DeadlineOverdueException("Deadline is overdue by "
                    + overdueDuration.toHours() + " hour(s).");
        }
        return "Time until deadline - "
                + Duration.between(LocalDateTime.now(), task.getDeadLineTime()).toHours()
                + " hour(s).";
    }

    @Override
    public int calculateTaskBudget(Long id) {
        Task task = findById(id);

        int numberOfEmployees = task.getTaskEmployees() != null
                ? task.getTaskEmployees().size() : 0;
        int baseBudget = numberOfEmployees * 1000;

        return switch (task.getPriority()) {
            case FIRST_PRIORITY -> baseBudget * 4;
            case SECOND_PRIORITY -> baseBudget * 3;
            case THIRD_PRIORITY -> baseBudget * 2;
            default -> baseBudget;
        };
    }

    @Override
    public Map<String, Double> calculateSalaryDistribution(Long id) {
        Task task = findById(id);

        int taskBudget = calculateTaskBudget(id);
        double companyCut = taskBudget * 0.40;
        double managerCut = taskBudget * 0.20;
        double employeesCut = taskBudget * 0.40;

        int numberOfEmployees = task.getTaskEmployees().size();
        double eachEmployeeCut = employeesCut / numberOfEmployees;

        Map<String, Double> salaryDistribution = new HashMap<>();
        salaryDistribution.put("Company cut", companyCut);
        salaryDistribution.put("Manager cut", managerCut);
        salaryDistribution.put("All employees cut", employeesCut);
        salaryDistribution.put("Each employee cut", eachEmployeeCut);

        return salaryDistribution;
    }
}
