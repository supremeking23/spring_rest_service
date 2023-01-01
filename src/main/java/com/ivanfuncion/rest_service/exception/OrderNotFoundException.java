package com.ivanfuncion.rest_service.exception;

public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(Long id){
        super("Order with an order id: " + id + " not found");
    }
}
