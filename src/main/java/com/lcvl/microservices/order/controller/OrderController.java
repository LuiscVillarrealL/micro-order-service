package com.lcvl.microservices.order.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lcvl.microservices.order.dto.OrderRequest;
import com.lcvl.microservices.order.service.OrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
	
	 private final OrderService orderService;

	    @PostMapping
	    @ResponseStatus(HttpStatus.CREATED)
	    public String placeOrder(@RequestBody OrderRequest orderRequest) {
	        orderService.placeOrder(orderRequest);
	        return "Order Placed Successfully";
	    }

}
