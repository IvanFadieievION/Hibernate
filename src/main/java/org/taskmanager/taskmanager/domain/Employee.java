package org.taskmanager.taskmanager.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Table(name = "employees")
@Data
@Accessors(chain = true)
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    @OneToOne
    private Department department;
    private boolean isManager;
}
