package com.lcvl.microservices.order.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.lcvl.microservices.order.client.InventoryClient;
import com.lcvl.microservices.order.dto.OrderRequest;
import com.lcvl.microservices.order.event.OrderPlacedEvent;
import com.lcvl.microservices.order.model.Order;
import com.lcvl.microservices.order.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
	
	private final OrderRepository orderRepository;
	private final InventoryClient inventoryClient;	
	private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;
	
	public void placeOrder(OrderRequest orderRequest) {
		
		var isProductIsInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());
		
		//TODO  Map OrderRequest to order object
		
		if(isProductIsInStock) {
			 Order order = new Order();
	         order.setOrderNumber(UUID.randomUUID().toString());
	         order.setPrice(orderRequest.price().multiply(BigDecimal.valueOf(orderRequest.quantity())));
	         order.setSkuCode(orderRequest.skuCode());
	         order.setQuantity(orderRequest.quantity());
	         orderRepository.save(order);
	         
	         //send message to kafka
	         OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent(order.getOrderNumber(), 
	        		 orderRequest.userDetails().email());
	         log.info("Start - Sending OrderPlacedEvent {} to kafta topic", orderPlacedEvent);
	         kafkaTemplate.send("order-placed", orderPlacedEvent);
	         log.info("End - Sending OrderPlacedEvent {} to kafta topic", orderPlacedEvent);
		}else {
			throw new RuntimeException("Product with skucode " + orderRequest.skuCode() + "is not in stock");
		}

	}

}
