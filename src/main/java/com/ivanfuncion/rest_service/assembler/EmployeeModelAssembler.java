package com.ivanfuncion.rest_service.assembler;

import com.ivanfuncion.rest_service.controller.EmployeeController;
import com.ivanfuncion.rest_service.model.Employee;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

public class EmployeeModelAssembler implements RepresentationModelAssembler<Employee, EntityModel<Employee>> {


    @Override
    public EntityModel<Employee> toModel(Employee employee) {
        return EntityModel.of(employee,
                linkTo(methodOn(EmployeeController.class).getSingleEmployee(employee.getId())).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).getAllEmployees()).withRel("employees"));
    }
}
