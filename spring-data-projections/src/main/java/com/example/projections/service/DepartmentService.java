package com.example.projections.service;

import com.example.projections.model.Department;
import com.example.projections.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    public Department getDepartment(Long id) {
        return departmentRepository.findById(id).orElse(null);
    }

    public Department updateDepartment(Long id, Department updatedDepartment) {
        if (departmentRepository.existsById(id)) {
            updatedDepartment.setId(id);
            return departmentRepository.save(updatedDepartment);
        }
        return null;
    }

    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }
}