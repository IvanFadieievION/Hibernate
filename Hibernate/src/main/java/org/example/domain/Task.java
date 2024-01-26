package org.example.domain;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.apache.commons.lang3.builder.ToStringExclude;

import org.example.enums.Priority;

@Entity
@Table(name = "tasks")
@Data
@Accessors(chain = true)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task id")
    private Long id;
    @Column(name = "task name")
    private String name;
    @Column(name = "task start time")
    private LocalDateTime startTime;
    @Column(name = "task deadline time")
    private LocalDateTime deadLineTime;
    @OneToMany(cascade = CascadeType.MERGE)
    @JoinColumn(name = "task_id")
    @Column(name = "task employees")
    @ToStringExclude
    @EqualsAndHashCode.Exclude
    private List<Employee> taskEmployees;
    @Enumerated(EnumType.STRING)
    @Column(name = "task priority")
    private Priority priority;
}
