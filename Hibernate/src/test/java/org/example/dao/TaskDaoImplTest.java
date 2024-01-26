package org.example.dao;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

import org.example.dao.impl.TaskDaoImpl;
import org.example.domain.Task;
import org.example.enums.Priority;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TaskDaoImplTest {
    private static final GenericDao<Task> taskDao = new TaskDaoImpl();
    public static final long INVALID_ID = -100L;

    @Test
    public void save_ValidTask_ReturnsTask() {
        Task task = createTask();

        Task actual = taskDao.save(task);

        assertNotNull(actual);
        assertEquals(actual, task);
    }

    @Test
    public void find_ValidId_ReturnsTask() {
        Task task = createTask();
        Task savedTask = taskDao.save(task);

        Optional<Task> actualTask = taskDao.find(savedTask.getId());

        assertNotNull(actualTask.get());
        assertEquals(savedTask, actualTask.get());
    }

    @Test
    public void find_InValidId_ReturnsFalse() {
        Optional<Task> actualTask = taskDao.find(INVALID_ID);

        assertFalse(actualTask.isPresent());
    }

    @Test
    public void update_ValidTask_ReturnsTask() {
        Task task = createTask();
        taskDao.save(task);
        task.setName("UpdatedTask");

        Task actual = taskDao.update(task);

        assertNotNull(actual);
        assertEquals(actual, task);
    }

    @Test
    public void update_InvalidTask_ReturnsFalse() {
        Task updatedTask = new Task()
                .setId(INVALID_ID)
                .setName("updatedTestTask");
        assertThrows(RuntimeException.class,
                () -> taskDao.update(updatedTask));
    }

    @Test
    public void delete_ValidId_ReturnsEmptyOptional() {
        Task task = createTask();
        Long taskId = taskDao.save(task).getId();

        taskDao.delete(taskId);

        assertFalse(taskDao.find(taskId).isPresent());
    }

    @Test
    public void deleteInvalidId_ThrowsException() {
        assertThrows(RuntimeException.class, () -> taskDao.delete(INVALID_ID));
    }

    private Task createTask() {
        return new Task()
                .setName("TestTask")
                .setStartTime(LocalDateTime.now()
                        .truncatedTo(ChronoUnit.SECONDS))
                .setTaskEmployees(List.of())
                .setDeadLineTime(LocalDateTime.now().plusDays(1L)
                        .truncatedTo(ChronoUnit.SECONDS))
                .setPriority(Priority.FIRST_PRIORITY);
    }
}
