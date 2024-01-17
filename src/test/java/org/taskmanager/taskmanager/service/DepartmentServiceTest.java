package org.taskmanager.taskmanager.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.taskmanager.taskmanager.domain.Department;
import org.taskmanager.taskmanager.exception.DepartmentNotFoundException;
import org.taskmanager.taskmanager.repository.DepartmentRepository;
import org.taskmanager.taskmanager.service.impl.DepartmentServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {
    @Mock
    private DepartmentRepository departmentRepository;
    @InjectMocks
    private DepartmentServiceImpl departmentService;
    private static Department testDepartment;
    private static final Long TEST_DEPARTMENT_ID = 1L;

    @BeforeEach
    void setUp() {
        testDepartment = new Department()
                .setId(0L)
                .setName("Accounting")
                .setEmployeesQuantity(3);
    }

    @Test
    public void save_ValidDepartment_ReturnsDepartment() {
        when(departmentRepository.save(testDepartment)).thenReturn(testDepartment);

        Department department = departmentService.create(testDepartment);

        assertNotNull(department);
        assertEquals(department, testDepartment);
    }

    @Test
    public void findById_ValidId_ReturnsDepartment() {
        when(departmentRepository.findById(TEST_DEPARTMENT_ID))
                .thenReturn(Optional.of(testDepartment));

        Department department = departmentService.findById(TEST_DEPARTMENT_ID);

        assertNotNull(department);
        assertEquals(department, testDepartment);
    }

    @Test
    public void findById_InValidId_ThrowsException() {
        Long invalidId = -1L;

        assertThrows(DepartmentNotFoundException.class, ()
                -> departmentService.findById(invalidId));
    }

    @Test
    public void update_ValidDepartment_UpdatesDepartment() {
        Department departmentForUpdate = new Department()
                .setId(2L)
                .setName("Updated departmentForUpdate")
                .setEmployeesQuantity(100);

        when(departmentRepository.save(departmentForUpdate)).thenReturn(departmentForUpdate);
        Department updatedDepartment = departmentService.update(TEST_DEPARTMENT_ID, departmentForUpdate);

        assertNotNull(updatedDepartment);
        assertEquals(TEST_DEPARTMENT_ID, updatedDepartment.getId());
    }

    @Test
    public void delete_ValidId_DeletesEmployee() {
        departmentService.deleteById(TEST_DEPARTMENT_ID);

        verify(departmentRepository, times(1)).deleteById(TEST_DEPARTMENT_ID);
    }
}
