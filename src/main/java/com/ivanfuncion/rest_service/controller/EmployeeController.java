package com.ivanfuncion.rest_service.controller;

import com.ivanfuncion.rest_service.exception.EmployeeNotFoundException;
import com.ivanfuncion.rest_service.model.Employee;
import com.ivanfuncion.rest_service.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/employees")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    @GetMapping(path = "/all")
    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

 //   @GetMapping(path = "/{id}")
//    public Employee getSingleEmployee(@PathVariable Long id){
//        return employeeRepository.findById(id).orElseThrow(()-> new EmployeeNotFoundException(id));
//    }
    @GetMapping(path = "/{id}")
    public EntityModel<Employee> getSingleEmployee(@PathVariable Long id){
        Employee employee = employeeRepository.findById(id).orElseThrow(()-> new EmployeeNotFoundException(id));

        return EntityModel.of(employee,
                linkTo(methodOn(EmployeeController.class).getSingleEmployee(id)).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).getAllEmployees()).withRel("employees_mo")
                );
    }

    @PostMapping(path = "/add")
    public Employee addEmployee(@RequestBody Employee newEmployee){
        return employeeRepository.save(newEmployee);
    }

    @PutMapping(path = "/{id}")
    public Employee updateEmployee(@RequestBody Employee newEmployee,@PathVariable Long id){
        return employeeRepository.findById(id).map(employee -> {
            employee.setName(newEmployee.getName());
            employee.setRole(newEmployee.getRole());
            return employeeRepository.save(employee);
        }).orElseGet(() ->{
            newEmployee.setId(id);
            return employeeRepository.save(newEmployee);
        });
    }

    @DeleteMapping(path = "/{id}")
    public void deleteEmployee(@PathVariable Long id){
        employeeRepository.deleteById(id);
    }

    //https://spring.io/guides/tutorials/rest/
}
