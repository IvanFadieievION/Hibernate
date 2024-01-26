package org.example.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "employees")
@Data
@Accessors(chain = true)
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee id")
    private Long id;
    @Column(name = "employee name")
    private String name;
    @Column(name = "employee surname")
    private String surname;
    @ManyToOne
    @JoinColumn(name = "department id")
    private Department department;
    @Column(name = "is manager")
    private boolean isManager;
}
