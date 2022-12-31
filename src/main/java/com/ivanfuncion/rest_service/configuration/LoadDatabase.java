package com.ivanfuncion.rest_service.configuration;

import com.ivanfuncion.rest_service.model.Employee;
import com.ivanfuncion.rest_service.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository employeeRepository){
        return args ->
        {
//            log.info("preloading" + employeeRepository.save(new Employee("Mavis Vermillion", "wizard")));
//            log.info("preloading" + employeeRepository.save(new Employee("Ivan ", "devils child")));
        };
    }

}

