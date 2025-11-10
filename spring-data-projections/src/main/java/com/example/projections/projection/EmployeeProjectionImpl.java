package com.example.projections.projection;

public class EmployeeProjectionImpl implements EmployeeProjection {
    private String fullName;
    private String position;
    private String departmentName;

    public EmployeeProjectionImpl(String fullName, String position, String departmentName) {
        this.fullName = fullName;
        this.position = position;
        this.departmentName = departmentName;
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public String getPosition() {
        return position;
    }

    @Override
    public String getDepartmentName() {
        return departmentName;
    }
}