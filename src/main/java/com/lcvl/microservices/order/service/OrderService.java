package com.lcvl.microservices.order.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.lcvl.microservices.order.dto.OrderRequest;
import com.lcvl.microservices.order.model.Order;
import com.lcvl.microservices.order.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
	
	private final OrderRepository orderRepository;
	
	public void placeOrder(OrderRequest orderRequest) {
		
		//TODO  Map OrderRequest to order object
		
		 Order order = new Order();
         order.setOrderNumber(UUID.randomUUID().toString());
         order.setPrice(orderRequest.price().multiply(BigDecimal.valueOf(orderRequest.quantity())));
         order.setSkuCode(orderRequest.skuCode());
         order.setQuantity(orderRequest.quantity());
         orderRepository.save(order);
	}

}
