package org.taskmanager.taskmanager.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.taskmanager.taskmanager.domain.Department;
import org.taskmanager.taskmanager.domain.Employee;
import org.taskmanager.taskmanager.domain.Task;
import org.taskmanager.taskmanager.enums.Priority;
import org.taskmanager.taskmanager.exception.*;
import org.taskmanager.taskmanager.repository.TaskRepository;
import org.taskmanager.taskmanager.service.impl.TaskServiceImpl;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private EmployeeService employeeService;
    @InjectMocks
    private TaskServiceImpl taskService;
    private final Department accounting = new Department()
            .setName("Accounting")
            .setEmployeesQuantity(3);
    private final Employee FIRST_EMPLOYEE = new Employee()
            .setId(0L)
            .setName("John")
            .setSurname("Doe")
            .setDepartment(accounting)
            .setManager(false);
    private final Employee SECOND_EMPLOYEE = new Employee()
            .setId(1L)
            .setName("Alice")
            .setSurname("Johnson")
            .setDepartment(accounting)
            .setManager(false);
    private final Employee THIRD_EMPLOYEE = new Employee()
            .setId(2L)
            .setName("Bob")
            .setSurname("Robinson")
            .setDepartment(accounting)
            .setManager(false);
    private final List<Employee> employees = List.of(FIRST_EMPLOYEE, SECOND_EMPLOYEE);
    private static final Long TEST_TASK_ID = 1L;
    private static Task testTask;

    @BeforeEach
    void setUp() {
        testTask = new Task()
                .setId(1L)
                .setName("Task for testing")
                .setStartTime(LocalDateTime.now())
                .setDeadLineTime(LocalDateTime.now().plusHours(24));
    }

    @Test
    public void createNewTask_ValidTask_ReturnsTask() {
        when(taskRepository.save(testTask)).thenReturn(testTask);

        Task returnedTask = taskService.createNewTask(testTask);

        assertNotNull(returnedTask);
        assertEquals(testTask, returnedTask);
    }

    @Test
    public void createNewTask_InvalidTask_ThrowsException() {
        Task invalidTask = new Task();

        assertThrows(TaskCreationException.class, () -> taskService.createNewTask(invalidTask));
    }

    @Test
    public void setNewDeadLine_ValidDeadline_UpdatesDeadline() {
        LocalDateTime newDeadLineTime = LocalDateTime.now().plusHours(40L);
        testTask.setDeadLineTime(newDeadLineTime);

        when(taskRepository.findById(TEST_TASK_ID)).thenReturn(Optional.of(testTask));
        taskService.setNewDeadLineTime(TEST_TASK_ID, newDeadLineTime);

        assertEquals(newDeadLineTime, testTask.getDeadLineTime());
    }

    @Test
    public void setNewDeadLine_InValidDeadline_ThrowsException() {
        LocalDateTime invalidNewDeadLineTime
                = testTask.getDeadLineTime().minusHours(2L);

        when(taskRepository.findById(TEST_TASK_ID)).thenReturn(Optional.of(testTask));

        assertThrows(InvalidDeadlineException.class, ()
                        -> taskService.setNewDeadLineTime(TEST_TASK_ID, invalidNewDeadLineTime));
    }

    @Test
    public void setTaskEmployees_ValidEmployees_UpdateEmployees() {
        when(taskRepository.findById(TEST_TASK_ID))
                .thenReturn(Optional.of(testTask));
        when(employeeService.findById(FIRST_EMPLOYEE.getId()))
                .thenReturn(FIRST_EMPLOYEE);
        when(taskRepository.findById(TEST_TASK_ID))
                .thenReturn(Optional.ofNullable(testTask));
        when(employeeService.findById(SECOND_EMPLOYEE.getId()))
                .thenReturn(SECOND_EMPLOYEE);
        when(taskRepository.save(testTask)).thenReturn(testTask);

        Task task = taskService.setNewTaskEmployees(TEST_TASK_ID, employees);

        assertEquals(task, testTask);
    }

    @Test
    public void addNewEmployeeToTask_ValidEmployee_UpdateEmployees() {
        testTask.setTaskEmployees(new ArrayList<>(Arrays.asList(FIRST_EMPLOYEE, SECOND_EMPLOYEE)));

        when(taskRepository.findById(TEST_TASK_ID))
                .thenReturn(Optional.of(testTask));
        when(employeeService.findById(THIRD_EMPLOYEE.getId()))
                .thenReturn(THIRD_EMPLOYEE);

        Employee addedNewEmployeeToTask
                = taskService.addNewEmployeeToTask(TEST_TASK_ID, THIRD_EMPLOYEE.getId());

        assertEquals(addedNewEmployeeToTask, THIRD_EMPLOYEE);
    }

    @Test
    public void addNewEmployeeToTask_InValidEmployee_ThrowsException() {
        testTask.setTaskEmployees(employees);

        when(taskRepository.findById(TEST_TASK_ID))
                .thenReturn(Optional.of(testTask));
        when(employeeService.findById(SECOND_EMPLOYEE.getId()))
                .thenReturn(SECOND_EMPLOYEE);

        assertThrows(EmployeeAlreadyAssignedException.class, ()
                -> taskService.addNewEmployeeToTask(TEST_TASK_ID, SECOND_EMPLOYEE.getId()));
    }

    @Test
    public void setNewTaskPriority_ValidPriority_UpdatePriority() {
        when(taskRepository.findById(TEST_TASK_ID)).thenReturn(Optional.of(testTask));

        taskService.setTaskPriority(TEST_TASK_ID, Priority.FIRST_PRIORITY);

        assertEquals(testTask.getPriority(), Priority.FIRST_PRIORITY);
    }

    @Test
    public void setNewTaskPriority_InValidPriority_ThrowsException() {
        when(taskRepository.findById(TEST_TASK_ID)).thenReturn(Optional.of(testTask));

        testTask.setPriority(Priority.FIRST_PRIORITY);

        assertThrows(PriorityValidationException.class, ()
                -> taskService.setTaskPriority(TEST_TASK_ID, Priority.FIRST_PRIORITY));
    }

    @Test
    public void calculateTaskTime_OneEmployee_ReturnsCorrectHours() {
        testTask.setTaskEmployees(List.of(FIRST_EMPLOYEE));

        when(taskRepository.findById(TEST_TASK_ID)).thenReturn(Optional.ofNullable(testTask));
        Long actual = taskService.calculateTaskTime(TEST_TASK_ID);
        Long expected = 24L;

        assertEquals(expected, actual);
    }

    @Test
    public void calculateTaskTime_TenEmployee_ReturnsCorrectHours() {
        List<Employee> tenEmployees = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            tenEmployees.add(FIRST_EMPLOYEE);
        }
        testTask.setTaskEmployees(tenEmployees);

        when(taskRepository.findById(TEST_TASK_ID)).thenReturn(Optional.ofNullable(testTask));
        Long actual = taskService.calculateTaskTime(TEST_TASK_ID);
        Long expected = 2L;

        assertEquals(expected, actual);
    }

    @Test
    public void calculateIsItEnoughTime_ValidTaskDuration_ReturnsPositiveResult() {
        testTask.setDeadLineTime(LocalDateTime.now().plusHours(25));
        testTask.setTaskEmployees(List.of(FIRST_EMPLOYEE));

        when(taskRepository.findById(TEST_TASK_ID)).thenReturn(Optional.ofNullable(testTask));
        String result = taskService.calculateIsItEnoughTime(TEST_TASK_ID);

        assertTrue(result.contains("There is enough time to complete the task."));
    }

    @Test
    public void calculateIsItEnoughTime_InValidTaskDuration_ReturnsNegativeResult() {
        testTask.setDeadLineTime(LocalDateTime.now().plusHours(8));
        testTask.setTaskEmployees(List.of(FIRST_EMPLOYEE));

        when(taskRepository.findById(TEST_TASK_ID)).thenReturn(Optional.ofNullable(testTask));
        String result = taskService.calculateIsItEnoughTime(TEST_TASK_ID);

        assertTrue(result.contains("Not enough time to complete the task."));
    }

    @Test
    public void calculateIfDeadLineIsNotOverDue_ReturnsAmountOfTimeLeft() {
        testTask.setDeadLineTime(LocalDateTime.now().plusHours(2L));

        when(taskRepository.findById(TEST_TASK_ID)).thenReturn(Optional.of(testTask));
        String result = taskService.calculateIfDeadLineIsNotOverDue(TEST_TASK_ID);

        assertTrue(result.contains("Time until deadline"));
    }

    @Test
    public void calculateIfDeadLineIsNotOverDue_ThrowsException() {
        testTask.setDeadLineTime(LocalDateTime.now().minusHours(2L));

        when(taskRepository.findById(TEST_TASK_ID)).thenReturn(Optional.of(testTask));

        assertThrows(DeadlineOverdueException.class, ()
                -> taskService.calculateIfDeadLineIsNotOverDue(TEST_TASK_ID));
    }

    @Test
    public void calculateTaskBudget_OneEmployee_FirstPriority_ReturnsCorrectValue() {
        testTask.setTaskEmployees(List.of(FIRST_EMPLOYEE));
        testTask.setPriority(Priority.FIRST_PRIORITY);

        when(taskRepository.findById(TEST_TASK_ID)).thenReturn(Optional.of(testTask));
        int actualBudget = taskService.calculateTaskBudget(TEST_TASK_ID);
        int expected = 4000;

        assertEquals(actualBudget, expected);
    }

    @Test
    public void calculateTaskBudget_FourEmployees_SecondPriority_ReturnsCorrectValue() {
        testTask.setPriority(Priority.SECOND_PRIORITY);
        testTask.setTaskEmployees(List.of(FIRST_EMPLOYEE, SECOND_EMPLOYEE, THIRD_EMPLOYEE));

        when(taskRepository.findById(TEST_TASK_ID)).thenReturn(Optional.of(testTask));
        int actualBudget = taskService.calculateTaskBudget(TEST_TASK_ID);
        int expected = 9000;

        assertEquals(actualBudget, expected);
    }

    @Test
    public void calculateTaskBudget_ZeroEmployees_FourthPriority_ReturnsZero() {
        testTask.setPriority(Priority.FOURTH_PRIORITY);

        when(taskRepository.findById(TEST_TASK_ID)).thenReturn(Optional.of(testTask));
        int actualBudget = taskService.calculateTaskBudget(TEST_TASK_ID);
        int expected = 0;

        assertEquals(actualBudget, expected);
    }

    @Test
    public void calculateSalaryDistribution_OneEmployee_FirstPriority_ReturnsCorrectValue() {
        testTask.setPriority(Priority.FIRST_PRIORITY);
        testTask.setTaskEmployees(List.of(FIRST_EMPLOYEE));

        Double expectedCompanyCut = 1600.0;
        Double expectedManagerCut = 800.0;
        Double expectedAllEmployeesCut = 1600.0;
        Double expectedEachEmployeeCut = 1600.0;

        when(taskRepository.findById(TEST_TASK_ID)).thenReturn(Optional.of(testTask));
        Map<String, Double> salaryDistribution
                = taskService.calculateSalaryDistribution(TEST_TASK_ID);

        assertEquals(salaryDistribution.get("Company cut"), expectedCompanyCut);
        assertEquals(salaryDistribution.get("Manager cut"), expectedManagerCut);
        assertEquals(salaryDistribution.get("All employees cut"), expectedAllEmployeesCut);
        assertEquals(salaryDistribution.get("Each employee cut"), expectedEachEmployeeCut);
    }

    @Test
    public void calculateSalaryDistribution_ThreeEmployee_FourthPriority_ReturnsCorrectValue() {
        testTask.setPriority(Priority.FOURTH_PRIORITY);
        testTask.setTaskEmployees(List.of(FIRST_EMPLOYEE, SECOND_EMPLOYEE, THIRD_EMPLOYEE));

        Double expectedCompanyCut = 1200.0;
        Double expectedManagerCut = 600.0;
        Double expectedAllEmployeesCut = 1200.0;
        Double expectedEachEmployeeCut = 400.0;

        when(taskRepository.findById(TEST_TASK_ID)).thenReturn(Optional.of(testTask));
        Map<String, Double> salaryDistribution
                = taskService.calculateSalaryDistribution(TEST_TASK_ID);

        assertEquals(salaryDistribution.get("Company cut"), expectedCompanyCut);
        assertEquals(salaryDistribution.get("Manager cut"), expectedManagerCut);
        assertEquals(salaryDistribution.get("All employees cut"), expectedAllEmployeesCut);
        assertEquals(salaryDistribution.get("Each employee cut"), expectedEachEmployeeCut);
    }
}
