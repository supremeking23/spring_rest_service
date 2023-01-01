package com.ivanfuncion.rest_service.repository;

import com.ivanfuncion.rest_service.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
