package com.example.projections.projection;

public interface EmployeeProjection {
    String getFullName();  // Имя сотрудника (firstName + lastName)
    String getPosition();  // Должность сотрудника
    String getDepartmentName();  // Название отдела
}
