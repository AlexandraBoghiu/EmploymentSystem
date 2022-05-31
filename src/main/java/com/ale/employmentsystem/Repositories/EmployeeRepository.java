package com.ale.employmentsystem.Repositories;

import com.ale.employmentsystem.Entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}