package org.taskmanager.taskmanager.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.taskmanager.taskmanager.domain.Department;
import org.taskmanager.taskmanager.domain.Employee;
import org.taskmanager.taskmanager.exception.EmployeeNotFoundException;
import org.taskmanager.taskmanager.repository.EmployeeRepository;
import org.taskmanager.taskmanager.service.impl.EmployeeServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private DepartmentService departmentService;
    @InjectMocks
    private EmployeeServiceImpl employeeService;
    private static Employee testEmployee;
    private static Long TEST_EMPLOYEE_ID = 0L;

    private final Department testDepartment = new Department()
            .setId(0L)
            .setName("Accounting")
            .setEmployeesQuantity(3);

    @BeforeEach
    void setUp() {
        testEmployee = new Employee()
                .setId(0L)
                .setName("Test name")
                .setSurname("Test surname")
                .setDepartment(testDepartment)
                .setManager(false);
    }

    @Test
    public void save_ValidEmployee_ReturnsEmployee() {
        when(employeeRepository.save(testEmployee)).thenReturn(testEmployee);
        when(departmentService.findById(testDepartment.getId())).thenReturn(testDepartment);

        Employee saved = employeeService.save(testEmployee);

        assertNotNull(saved);
        assertEquals(saved, testEmployee);
    }

    @Test
    public void findById_ValidId_ReturnsEmployee() {
        when(employeeRepository.findById(TEST_EMPLOYEE_ID)).thenReturn(Optional.of(testEmployee));

        Employee employeeById = employeeService.findById(TEST_EMPLOYEE_ID);

        assertNotNull(employeeById);
        assertEquals(employeeById, testEmployee);
    }

    @Test
    public void findById_InValidId_ThrowsException() {
        Long invalidId = -1L;

        assertThrows(EmployeeNotFoundException.class, ()
                -> employeeService.findById(invalidId));
    }

    @Test
    public void update_ValidEmployee_UpdatesEmployee() {
        Employee updatedEmployee = new Employee()
                .setId(100L)
                .setName("Updated name")
                .setSurname("Updated surname")
                .setManager(true);

        when(employeeRepository.save(updatedEmployee)).thenReturn(updatedEmployee);
        Employee updated = employeeService.update(updatedEmployee, TEST_EMPLOYEE_ID);

        assertNotNull(updated);
        assertEquals(TEST_EMPLOYEE_ID, updated.getId());
    }

    @Test
    public void delete_ValidId_DeletesEmployee() {
        employeeService.deleteById(TEST_EMPLOYEE_ID);

        verify(employeeRepository, times(1)).deleteById(TEST_EMPLOYEE_ID);
    }
}
