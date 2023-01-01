package com.ivanfuncion.rest_service.assembler;

import com.ivanfuncion.rest_service.controller.OrderController;
import com.ivanfuncion.rest_service.enumerable.Status;
import com.ivanfuncion.rest_service.model.Order;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>> {
    @Override
    public EntityModel<Order> toModel(Order order) {

        // Unconditional links to single-item resource and aggregate root
        EntityModel<Order> orderModel = EntityModel.of(order,
                linkTo(methodOn(OrderController.class).allOrders()).withRel("orders"),
                linkTo(methodOn(OrderController.class).findSingleOrder(order.getId())).withSelfRel()
                );

        if(order.getStatus() == Status.IN_PROGRESS){
            orderModel.add(linkTo(methodOn(OrderController.class).completeOrder(order.getId())).withRel("complete_order"));
            orderModel.add(linkTo(methodOn(OrderController.class).cancelOrder(order.getId())).withRel("cancel_order"));
        }

        return orderModel;
    }
}
