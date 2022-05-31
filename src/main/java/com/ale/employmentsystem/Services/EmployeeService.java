package com.ale.employmentsystem.Services;

import com.ale.employmentsystem.Entities.Employee;
import com.ale.employmentsystem.Repositories.EmployeeRepository;
import com.vaadin.flow.data.binder.Binder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    public Employee addEmployee(Employee employee) throws ParseException {
//        System.out.println(data.get(0));
//        System.out.println(data.get(1));
//        System.out.println(data.get(2));
//        System.out.println(data.get(3));
//        System.out.println(data.get(4));
        System.out.println(employee);
        return this.repository.save(employee);
    }


}
