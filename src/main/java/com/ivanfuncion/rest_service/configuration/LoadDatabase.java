package com.ivanfuncion.rest_service.configuration;

import com.ivanfuncion.rest_service.enumerable.Status;
import com.ivanfuncion.rest_service.model.Employee;
import com.ivanfuncion.rest_service.model.Order;
import com.ivanfuncion.rest_service.repository.EmployeeRepository;
import com.ivanfuncion.rest_service.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository employeeRepository, OrderRepository orderRepository){
        return args ->
        {
//            log.info("preloading" + employeeRepository.save(new Employee("Mavis Vermillion", "wizard")));
//            log.info("preloading" + employeeRepository.save(new Employee("Ivan ", "devils child")));
            log.info("preloading" + employeeRepository.save(new Employee("Mavis ", "Vermillion", "devils child")));
            log.info("preloading" + employeeRepository.save(new Employee("Zeref ", "Dragneel", "Black Wizard")));
            log.info("preloading" + employeeRepository.save(new Employee("Angurie ", "Rice", "Actress")));

            employeeRepository.findAll().forEach(employee -> log.info("Preloaded" + employee));

            orderRepository.save(new Order("Macbook pro", Status.COMPLETED));
            orderRepository.save(new Order("Macbook pro bente", Status.IN_PROGRESS));

            orderRepository.findAll().forEach(order -> {
                log.info("Preloaded " + order);
            });

        };
    }

}

