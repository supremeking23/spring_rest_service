package com.ivanfuncion.rest_service.controller;

import com.ivanfuncion.rest_service.assembler.OrderAssembler;
import com.ivanfuncion.rest_service.enumerable.Status;
import com.ivanfuncion.rest_service.exception.OrderNotFoundException;
import com.ivanfuncion.rest_service.model.Order;
import com.ivanfuncion.rest_service.repository.OrderRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping(path = "/orders")
public class OrderController {
    private final OrderRepository orderRepository;
    // for assembler
    private final OrderAssembler orderAssembler;

    public OrderController(OrderRepository orderRepository, OrderAssembler orderAssembler){
        this.orderRepository = orderRepository;
        this.orderAssembler = orderAssembler;
    }

    @GetMapping(path = "/all")
    public CollectionModel<EntityModel<Order>> allOrders(){
        List<EntityModel<Order>> orders = orderRepository.findAll().stream()
                .map(orderAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(orders, linkTo(methodOn(OrderController.class).allOrders()).withSelfRel());

    }

    @GetMapping(path = "/{id}")
    public EntityModel<Order> findSingleOrder(@PathVariable Long id){
        Order order = orderRepository.findById(id).orElseThrow(()-> new OrderNotFoundException(id));

        return orderAssembler.toModel(order);
    }

    @PostMapping(path = "/add")
    public ResponseEntity<EntityModel<Order>> newOrder(@RequestBody Order order){
        order.setStatus(Status.IN_PROGRESS); // or toString
        Order newOrder = orderRepository.save(order);

        return ResponseEntity.created(
                linkTo(methodOn(OrderController.class).findSingleOrder(order.getId())).toUri()
        ).body(orderAssembler.toModel(newOrder));
    }


    @PutMapping(path = "/{id}/complete")
    public ResponseEntity<?> completeOrder(@PathVariable Long id){
        Order order = orderRepository.findById(id).orElseThrow(()-> new OrderNotFoundException(id));
        if(order.getStatus() == Status.IN_PROGRESS){
            order.setStatus(Status.COMPLETED);
        }

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create().withTitle("Method not allowed lods").withDetail("Ypu can't complete an order that is in the " + order.getStatus() + " status"));
    }


    @DeleteMapping(path = "/{id}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id){
        Order order = orderRepository.findById(id).orElseThrow(()-> new OrderNotFoundException(id));

        if(order.getStatus() == Status.IN_PROGRESS){
            order.setStatus(Status.CANCELLED);
            return ResponseEntity.ok(orderAssembler.toModel(orderRepository.save(order)));
        }

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create().withTitle("Method not allowed").withDetail("You can;t cancel an order that is in the " + order.getStatus() + " status")
                );
    }

}
