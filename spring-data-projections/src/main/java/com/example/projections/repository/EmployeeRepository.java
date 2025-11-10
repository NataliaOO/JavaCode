package com.example.projections.repository;

import com.example.projections.model.Employee;
import com.example.projections.projection.EmployeeProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}