package org.example.dao;

import java.util.Optional;
import org.junit.jupiter.api.Test;

import org.example.dao.impl.EmployeeDaoImpl;
import org.example.domain.Employee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EmployeeDaoImplTest {
    private static final GenericDao<Employee> employeeDao = new EmployeeDaoImpl();
    private static final Long INVALID_ID = -100L;

    @Test
    public void save_ValidEmployee_ReturnsEmployee() {
        Employee employee = createEmployee();

        Employee actual = employeeDao.save(employee);

        assertNotNull(actual);
        assertEquals(actual, employee);
    }

    @Test
    public void find_ValidId_ReturnsEmployee() {
        Employee employee = createEmployee();
        Employee savedEmployee = employeeDao.save(employee);

        Optional<Employee> actualEmployee = employeeDao.find(savedEmployee.getId());

        assertNotNull(actualEmployee.get());
        assertEquals(savedEmployee, actualEmployee.get());
    }

    @Test
    public void find_InValidId_ReturnsFalse() {
        Optional<Employee> actualEmployee = employeeDao.find(INVALID_ID);

        assertFalse(actualEmployee.isPresent());
    }

    @Test
    public void update_ValidEmployee_ReturnsEmployee() {
        Employee employee = createEmployee();
        employeeDao.save(employee);
        employee.setName("UpdatedEmployee");

        Employee actual = employeeDao.update(employee);

        assertNotNull(actual);
        assertEquals(actual, employee);
    }

    @Test
    public void update_InvalidEmployee_ReturnsFalse() {
        Employee updatedEmployee = new Employee()
                .setId(INVALID_ID)
                .setName("UpdatedEmployee");
        assertThrows(RuntimeException.class,
                () -> employeeDao.update(updatedEmployee));
    }

    @Test
    public void delete_ValidId_ReturnsEmptyOptional() {
        Employee employee = createEmployee();
        Long employeeId = employeeDao.save(employee).getId();

        employeeDao.delete(employeeId);

        assertFalse(employeeDao.find(employeeId).isPresent());
    }

    @Test
    public void deleteInvalidId_ThrowsException() {
        assertThrows(RuntimeException.class, ()
                -> employeeDao.delete(INVALID_ID));
    }

    private Employee createEmployee() {
        return new Employee()
                .setName("TestEmployeeName")
                .setSurname("TestEmployeeSurname")
                .setManager(false);
    }
}
