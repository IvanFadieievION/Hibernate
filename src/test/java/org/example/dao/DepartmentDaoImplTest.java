package org.example.dao;

import java.util.Optional;
import org.junit.jupiter.api.Test;

import org.example.dao.impl.DepartmentDaoImpl;
import org.example.domain.Department;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DepartmentDaoImplTest {
    private static final GenericDao<Department> departmentDao = new DepartmentDaoImpl();
    public static final long INVALID_ID = -100L;

    @Test
    public void save_ValidDepartment_ReturnsDepartment() {
        Department department = createDepartment();

        Department actual = departmentDao.save(department);

        assertNotNull(actual);
        assertEquals(actual, department);
    }

    @Test
    public void find_ValidId_ReturnsDepartment() {
        Department department = createDepartment();
        Department savedDepartment = departmentDao.save(department);

        Optional<Department> actualDepartment = departmentDao.find(savedDepartment.getId());

        assertNotNull(actualDepartment.get());
        assertEquals(department, actualDepartment.get());
    }

    @Test
    public void find_InValidId_ReturnsFalse() {
        Optional<Department> actualDepartment = departmentDao.find(INVALID_ID);

        assertFalse(actualDepartment.isPresent());
    }

    @Test
    public void update_ValidDepartment_ReturnsDepartment() {
        Department department = createDepartment();
        departmentDao.save(department);
        department.setName("UpdatedDepartment");

        Department actual = departmentDao.update(department);

        assertNotNull(actual);
        assertEquals(actual, department);
    }

    @Test
    public void update_InvalidDepartment_ReturnsFalse() {
        Department updatedDepartment = new Department()
                .setId(INVALID_ID)
                .setName("UpdatedDepartment")
                .setEmployeesQuantity(20);
        assertThrows(RuntimeException.class,
                () -> departmentDao.update(updatedDepartment));
    }

    @Test
    public void delete_ValidId_ReturnsEmptyOptional() {
        Department department = createDepartment();
        Long departmentId = departmentDao.save(department).getId();

        departmentDao.delete(departmentId);

        assertFalse(departmentDao.find(departmentId).isPresent());
    }

    @Test
    public void deleteInvalidId_ThrowsException() {
        assertThrows(RuntimeException.class, () -> departmentDao.delete(INVALID_ID));
    }

    private Department createDepartment() {
        return new Department()
                .setName("TestDepartment")
                .setEmployeesQuantity(100);
    }
}
