package com.ale.employmentsystem;

import com.ale.employmentsystem.Entities.Employee;
import com.ale.employmentsystem.Repositories.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository repository) {

        return args -> {
            log.info("Preloading " + repository.save(new Employee("Anne", "Sophie", "Jackson", new SimpleDateFormat("yyyy-MM-dd").parse("1980-12-22"), "CEO")));
        };
    }
}