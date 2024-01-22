package com.example.spring_test.api.controller.order;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_test.api.ApiResponse;
import com.example.spring_test.api.controller.order.request.OrderCreateRequest;
import com.example.spring_test.api.service.order.OrderService;
import com.example.spring_test.api.service.order.response.OrderResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@PostMapping("/api/v1/orders/new")
	public ApiResponse<OrderResponse> createOrder(@Valid @RequestBody OrderCreateRequest request) {
		LocalDateTime registeredDateTime = LocalDateTime.now();
		return ApiResponse.ok(orderService.createOrder(request.toServiceRequest(), registeredDateTime));
	}
	
}
