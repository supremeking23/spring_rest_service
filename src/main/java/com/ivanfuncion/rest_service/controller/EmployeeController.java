package com.ivanfuncion.rest_service.controller;

import com.ivanfuncion.rest_service.assembler.EmployeeModelAssembler;
import com.ivanfuncion.rest_service.exception.EmployeeNotFoundException;
import com.ivanfuncion.rest_service.model.Employee;
import com.ivanfuncion.rest_service.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/employees")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private final EmployeeModelAssembler employeeModelAssembler;

    public EmployeeController(EmployeeRepository employeeRepository, EmployeeModelAssembler employeeModelAssembler){
        this.employeeRepository = employeeRepository;
        this.employeeModelAssembler = employeeModelAssembler;
    }

//    @GetMapping(path = "/all")
//    public List<Employee> getAllEmployees(){
//        return employeeRepository.findAll();
//    }

    //aggregate root
//    @GetMapping(path = "/all")
//    public CollectionModel<EntityModel<Employee>> getAllEmployees(){
//        //geting an aggregate root resource
//        List<EntityModel<Employee>> employees = employeeRepository.findAll()
//                .stream().map(employee -> EntityModel.of(employee,
//                        linkTo(methodOn(EmployeeController.class).getSingleEmployee(employee.getId())).withSelfRel(),
//                        linkTo(methodOn(EmployeeController.class).getAllEmployees()).withRel("employees"))).collect(Collectors.toList());
//
//        return CollectionModel.of(employees, linkTo(methodOn(EmployeeController.class).getAllEmployees()).withSelfRel());
//
//        //Since we’re talking REST, it should encapsulate collections of employee resources.
//    }


    // using assembler
    @GetMapping("/all")
    public CollectionModel<EntityModel<Employee>> getAllEmployees(){
        List<EntityModel<Employee>> employees = employeeRepository.findAll()
                .stream().map(employeeModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(employees, linkTo(
                methodOn(EmployeeController.class).getAllEmployees()
        ).withSelfRel());
    }

 //   @GetMapping(path = "/{id}")
//    public Employee getSingleEmployee(@PathVariable Long id){
//        return employeeRepository.findById(id).orElseThrow(()-> new EmployeeNotFoundException(id));
//    }
    @GetMapping(path = "/{id}")
    public EntityModel<Employee> getSingleEmployee(@PathVariable Long id){
        Employee employee = employeeRepository.findById(id).orElseThrow(()-> new EmployeeNotFoundException(id));

//        return EntityModel.of(employee,
//                linkTo(methodOn(EmployeeController.class).getSingleEmployee(id)).withSelfRel(),
//                linkTo(methodOn(EmployeeController.class).getAllEmployees()).withRel("employees_mo")
//                );
        //using assember
        return employeeModelAssembler.toModel(employee);
    }

//    @PostMapping(path = "/add")
//    public Employee addEmployee(@RequestBody Employee newEmployee){
//        return employeeRepository.save(newEmployee);
//    }

    // POST that handles old and new client requests
    @PostMapping(path = "/add")
    public ResponseEntity<?> addEmployee(@RequestBody Employee newEmployee){
        //The new Employee object is saved as before. But the resulting object is wrapped using the EmployeeModelAssembler.
        EntityModel<Employee> entityModel = employeeModelAssembler.toModel(employeeRepository.save(newEmployee));

        //Spring MVC’s ResponseEntity is used to create an HTTP 201 Created status message.
        // This type of response typically includes a Location response header, and we use the URI derived from the model’s self-related link.
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
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
