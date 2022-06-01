package com.ale.employmentsystem.Repositories;

import com.ale.employmentsystem.Entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findEmployeeByBirthDateAndLastNameAndFirstNameAndMiddleName(Date birthDate, String lastName, String firstName, String middleName);
}