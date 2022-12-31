package com.ivanfuncion.rest_service.exceptionAdvice;

import com.ivanfuncion.rest_service.exception.EmployeeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class EmployeeNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(EmployeeNotFoundException.class) // configures the advice to only respond if an EmployeeNotFoundException is thrown.
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String employeeNotFoundHandler(EmployeeNotFoundException exception){
        return exception.getMessage();
    }
}
