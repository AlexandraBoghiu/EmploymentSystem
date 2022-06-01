package com.ale.employmentsystem.Services;

import com.ale.employmentsystem.Entities.Employee;
import com.ale.employmentsystem.Exceptions.EmployeeDuplicateException;
import com.ale.employmentsystem.Repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository repository;
    @Autowired
    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public List<Employee> getEmployees(){
        return this.repository.findAll();
    }
    public void addEmployee(Employee employee){

            if (!employee.equals(this.repository.findEmployeeByBirthDateAndLastNameAndFirstNameAndMiddleName(employee.getBirthDate(), employee.getLastName(), employee.getFirstName(), employee.getMiddleName())))
                this.repository.save(employee);
            else
                throw new EmployeeDuplicateException();


    }

}
