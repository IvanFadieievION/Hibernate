package org.example.service;

import org.example.dao.GenericDao;
import org.example.dao.impl.DepartmentDaoImpl;
import org.example.dao.impl.EmployeeDaoImpl;
import org.example.dao.impl.TaskDaoImpl;
import org.example.domain.Department;
import org.example.domain.Employee;
import org.example.domain.Task;
import org.example.enums.Priority;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class App {
    private static final GenericDao<Department> departmentDao = new DepartmentDaoImpl();
    private static final GenericDao<Employee> employeeDao = new EmployeeDaoImpl();
    private static final GenericDao<Task> taskDao = new TaskDaoImpl();
    public static void run() {

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Choose available entities:");
            System.out.println("1. Department.");
            System.out.println("2. Employee.");
            System.out.println("3. Task.");
            System.out.println("0. Exit program.");
            switch (scanner.nextInt()) {
                case 1:
                    handleDepartmentOperations(scanner);
                    break;
                case 2:
                    handleEmployeeOperations(scanner);
                    break;
                case 3:
                    handleTaskOperations(scanner);
                    break;
                case 0:
                    System.out.println("Exiting the program.");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void handleTaskOperations(Scanner scanner) {
        System.out.println("Choose available options");
        System.out.println("1. Create task.");
        System.out.println("2. Find task by id.");
        System.out.println("3. Update task.");
        System.out.println("4. Delete task.");
        System.out.println("5. Show all tasks.");

        int taskOption = scanner.nextInt();

        switch (taskOption) {
            case 1:
                createNewTask(scanner);
                break;
            case 2:
                findTask(scanner);
                break;
            case 3:
                updateTask(scanner);
                break;
            case 4:
                deleteTask(scanner);
                break;
            case 5:
                findAllTasks();
                break;
            default:
                System.out.println("Invalid option for Task.");
        }
    }

    private static void createNewTask(Scanner scanner) {
        System.out.println("Enter task name: ");
        String taskName = scanner.next();
        System.out.println("Enter task start time [yyyy-MM-dd-HH-mm-ss]: ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        LocalDateTime startTime = LocalDateTime.parse(scanner.next(), formatter);
        System.out.println("Enter task deadline time [yyyy-MM-dd-HH-mm-ss]: ");
        LocalDateTime deadLine = LocalDateTime.parse(scanner.next(), formatter);
        System.out.println("Enter employee's id's: ");
        List<Employee> taskEmployees = getEmployees(scanner);
        System.out.println("Enter task priority: ");
        Priority taskPriority = Priority.valueOf(scanner.next());
        App.taskDao.save(new Task()
                .setName(taskName)
                .setStartTime(startTime)
                .setDeadLineTime(deadLine)
                .setTaskEmployees(taskEmployees)
                .setPriority(taskPriority));
    }

    private static void deleteTask(Scanner scanner) {
        System.out.println("Enter task id: ");
        App.taskDao.delete(scanner.nextLong());
    }

    private static void updateTask(Scanner scanner) {
        System.out.println("Enter task id: ");
        Long taskId = scanner.nextLong();
        System.out.println("Enter task name: ");
        String taskName = scanner.next();
        System.out.println("Enter task start time [yyyy-MM-dd-HH-mm-ss]: ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        LocalDateTime startTime = LocalDateTime.parse(scanner.next(), formatter);
        System.out.println("Enter task deadline time [yyyy-MM-dd-HH-mm-ss]: ");
        LocalDateTime deadLine = LocalDateTime.parse(scanner.next(), formatter);
        System.out.println("Enter employee's id's: ");
        List<Employee> taskEmployees = getEmployees(scanner);
        System.out.println("Enter task priority: ");
        Priority taskPriority = Priority.valueOf(scanner.next());
        App.taskDao.update(new Task()
                .setId(taskId)
                .setName(taskName)
                .setStartTime(startTime)
                .setDeadLineTime(deadLine)
                .setTaskEmployees(taskEmployees)
                .setPriority(taskPriority));
    }

    private static void findTask(Scanner scanner) {
        System.out.println("Enter task id: ");
        Optional<Task> employee = App.taskDao.find(scanner.nextLong());
        if (employee.isPresent()) {
            System.out.println(employee);
        }
    }

    private static void findAllTasks() {
        taskDao.findAll().forEach(System.out::println);
    }

    private static List<Employee> getEmployees(Scanner scanner) {
        String[] employeesIds = scanner.next().split(",");
        return Arrays.stream(employeesIds)
                .map(Long::parseLong)
                .map(id -> employeeDao.find(id).orElseThrow(()
                        -> new RuntimeException("Can't find employee by provided id")))
                .toList();
    }

    private static void handleEmployeeOperations(Scanner scanner) {
        System.out.println("Choose available options");
        System.out.println("1. Create employee.");
        System.out.println("2. Find employee by id.");
        System.out.println("3. Update employee.");
        System.out.println("4. Delete employee.");
        System.out.println("5. Show all employees.");

        int employeeOption = scanner.nextInt();

        switch (employeeOption) {
            case 1:
                createNewEmployee(scanner);
                break;
            case 2:
                findEmployee(scanner);
                break;
            case 3:
                updateEmployee(scanner);
                break;
            case 4:
                deleteEmployee(scanner);
                break;
            case 5:
                findAllEmployees();
                break;
            default:
                System.out.println("Invalid option for Employee.");
        }
    }

    private static void findAllEmployees() {
        employeeDao.findAll().forEach(System.out::println);
    }

    private static void createNewEmployee(Scanner scanner) {
        System.out.println("Enter employee name: ");
        String employeeName = scanner.next();
        System.out.println("Enter employee surname: ");
        String employeeSurname = scanner.next();
        System.out.println("Enter employee's department id: ");
        Long departmentId = scanner.nextLong();
        System.out.println("Is employee manager?");
        boolean isManager = scanner.nextBoolean();
        App.employeeDao.save(new Employee()
                .setName(employeeName)
                .setSurname(employeeSurname)
                .setDepartment(departmentDao.find(departmentId).orElseThrow(()
                        -> new RuntimeException("Can't find department by provided id")))
                .setManager(isManager));
    }

    private static void findEmployee(Scanner scanner) {
        System.out.println("Enter employee id: ");
        Optional<Employee> employee = App.employeeDao.find(scanner.nextLong());
        if (employee.isPresent()) {
            System.out.println(employee);
        }
    }

    private static void updateEmployee(Scanner scanner) {
        System.out.println("Enter employee id for updating: ");
        Long employeeId = scanner.nextLong();
        System.out.println("Enter employee name: ");
        String employeeName = scanner.next();
        System.out.println("Enter employee surname: ");
        String employeeSurname = scanner.next();
        System.out.println("Enter employee's department id: ");
        Long departmentId = scanner.nextLong();
        System.out.println("Is employee manager?");
        boolean isManager = scanner.nextBoolean();
        App.employeeDao.update(new Employee()
                .setId(employeeId)
                .setName(employeeName)
                .setSurname(employeeSurname)
                .setDepartment(departmentDao.find(departmentId).orElseThrow(()
                        -> new RuntimeException("Can't find department by provided id")))
                .setManager(isManager));
    }

    private static void deleteEmployee(Scanner scanner) {
        System.out.println("Enter employee id: ");
        App.employeeDao.delete(scanner.nextLong());
    }

    private static void handleDepartmentOperations(Scanner scanner) {
        System.out.println("Choose available options");
        System.out.println("1. Create department.");
        System.out.println("2. Find department by id.");
        System.out.println("3. Update department.");
        System.out.println("4. Delete department.");
        System.out.println("5. Show all departments.");

        int departmentOption = scanner.nextInt();

        switch (departmentOption) {
            case 1:
                createNewDepartment(scanner);
                break;
            case 2:
                findDepartment(scanner);
                break;
            case 3:
                updateDepartment(scanner);
                break;
            case 4:
                deleteDepartment(scanner);
                break;
            case 5:
                findAllDepartments();
                break;
            default:
                System.out.println("Invalid option for Department.");
        }
    }

    private static void findAllDepartments() {
        departmentDao.findAll().forEach(System.out::println);
    }

    private static void createNewDepartment(Scanner scanner) {
        System.out.println("Enter department name: ");
        String departmentName = scanner.next();
        System.out.println("Enter employees quantity: ");
        Integer departmentQuantities = scanner.nextInt();
        App.departmentDao.save(new Department()
                .setName(departmentName)
                .setEmployeesQuantity(departmentQuantities));
    }

    private static void findDepartment(Scanner scanner) {
        System.out.println("Enter department id: ");
        Optional<Department> department = App.departmentDao.find(scanner.nextLong());
        if (department.isPresent()) {
            System.out.println(department);
        }
    }

    private static void updateDepartment(Scanner scanner) {
        System.out.println("Enter department id: ");
        Long departmentIdForUpdating = scanner.nextLong();
        System.out.println("Enter department name: ");
        String departmentNameForUpdating = scanner.next();
        System.out.println("Enter employees quantity: ");
        Integer departmentQuantitiesForUpdating = scanner.nextInt();
        App.departmentDao.update(new Department()
                .setId(departmentIdForUpdating)
                .setName(departmentNameForUpdating)
                .setEmployeesQuantity(departmentQuantitiesForUpdating));
    }

    private static void deleteDepartment(Scanner scanner) {
        System.out.println("Enter department id: ");
        App.departmentDao.delete(scanner.nextLong());
    }
}
