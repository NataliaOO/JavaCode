package com.example.projections.controller;

import com.example.projections.model.Employee;
import com.example.projections.projection.EmployeeProjection;
import com.example.projections.repository.EmployeeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Получить всех сотрудников с проекцией
    @GetMapping
    public List<EmployeeProjection> getAllEmployees() {
        return employeeRepository.findAllProjectedBy();
    }

    // Получить сотрудника по id
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeProjection> getEmployeeById(@PathVariable Long id) {
        return employeeRepository.findById(id)
                .map(employee -> (EmployeeProjection) employee)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Создать нового сотрудника
    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }

    // Обновить сотрудника
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        if (employeeRepository.existsById(id)) {
            employee.setId(id);
            return ResponseEntity.ok(employeeRepository.save(employee));
        }
        return ResponseEntity.notFound().build();
    }

    // Удалить сотрудника
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}