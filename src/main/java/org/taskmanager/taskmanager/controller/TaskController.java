package org.taskmanager.taskmanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.taskmanager.taskmanager.domain.Employee;
import org.taskmanager.taskmanager.domain.Task;
import org.taskmanager.taskmanager.enums.Priority;
import org.taskmanager.taskmanager.service.TaskService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @PostMapping
    public Task create(@RequestBody Task task) {
        return taskService.createNewTask(task);
    }

    @GetMapping("/{id}")
    public Task getById(@PathVariable Long id) {
        return taskService.findById(id);
    }

    @PutMapping("/{id}")
    public Task update(@PathVariable Long id,
                       @RequestBody Task task) {
        return taskService.update(id, task);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        taskService.deleteById(id);
    }

    @PutMapping("/{id}/deadline")
    public Task setNewDeadLineTime(@PathVariable Long id,
                                   @RequestParam("deadline") LocalDateTime deadLineTime){
        return taskService.setNewDeadLineTime(id, deadLineTime);
    }

    @PutMapping("/{id}/employees/set")
    public Task setTaskEmployees(@PathVariable Long id,
                                 @RequestBody List<Employee> employees){
        return taskService.setNewTaskEmployees(id, employees);
    }

    @PutMapping("/{id}/employees/add")
    public Employee addNewEmployee(@PathVariable Long id,
                               @RequestBody Map<String, Object> employee){
        Integer employeeId = (Integer) employee.get("id");
        return taskService.addNewEmployeeToTask(id, (long) employeeId);
    }

    @PutMapping("/{id}/priority")
    public void setPriority(@PathVariable Long id,
                            @RequestBody Map<String, String> priority) {
        Priority priorityName = Priority.valueOf(priority.get("priority"));
        taskService.setTaskPriority(id, priorityName);
    }

    @GetMapping("/{id}/timerequired")
    public ResponseEntity<String> calculateTaskTime(@PathVariable Long id) {
        Long calculatedTaskTime = taskService.calculateTaskTime(id);
        return ResponseEntity.ok("Oriented time for task completion - "
                + calculatedTaskTime + " hour(s).");
    }

    @GetMapping("/{id}/checktime")
    public ResponseEntity<String> checkTime(@PathVariable Long id) {
        String calculationResult = taskService.calculateIsItEnoughTime(id);
        return ResponseEntity.ok(calculationResult);
    }

    @GetMapping("/{id}/deadline")
    public ResponseEntity<String> checkDeadline(@PathVariable Long id) {
        try {
            String calculationResult = taskService.calculateIfDeadLineIsNotOverDue(id);
            return ResponseEntity.ok(calculationResult);
        } catch (RuntimeException e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @GetMapping("/{id}/budget")
    public ResponseEntity<String> calculateTaskBudget(@PathVariable Long id) {
        return ResponseEntity.ok("Budget for this task is - "
                + taskService.calculateTaskBudget(id) + ".");
    }

    @GetMapping("/{id}/salaries")
    public ResponseEntity<Map<String, Double>> calculateSalariesDistribution(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.calculateSalaryDistribution(id));
    }
}
