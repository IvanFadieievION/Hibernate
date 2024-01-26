package org.example.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "departments")
@Data
@Accessors(chain = true)
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department id")
    private Long id;
    @Column(name = "department name")
    private String name;
    @Column(name = "employees quantity")
    private Integer employeesQuantity;
}
