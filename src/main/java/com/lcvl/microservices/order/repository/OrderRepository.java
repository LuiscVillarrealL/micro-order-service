package com.lcvl.microservices.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lcvl.microservices.order.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

}
