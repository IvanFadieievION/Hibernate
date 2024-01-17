package org.taskmanager.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.taskmanager.taskmanager.domain.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
