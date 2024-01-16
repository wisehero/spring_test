package com.example.spring_test.api.service.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.spring_test.api.controller.order.request.OrderCreateRequest;
import com.example.spring_test.api.service.order.response.OrderResponse;
import com.example.spring_test.domain.order.Order;
import com.example.spring_test.domain.order.OrderRepository;
import com.example.spring_test.domain.product.Product;
import com.example.spring_test.domain.product.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;

	public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime registeredDateTime) {
		List<String> productNumbers = request.getProductNumbers();
		List<Product> duplicateProducts = findProductBy(productNumbers);

		Order order = Order.create(duplicateProducts, registeredDateTime);
		Order savedOrder = orderRepository.save(order);
		return OrderResponse.of(savedOrder);
	}

	private List<Product> findProductBy(List<String> productNumbers) {
		List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);
		Map<String, Product> productMap = products.stream()
				.collect(Collectors.toMap(Product::getProductNumber, p -> p));

		return productNumbers.stream().map(productMap::get)
				.toList();
	}
}
