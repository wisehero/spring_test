package com.example.spring_test.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.spring_test.api.service.response.ProductResponse;
import com.example.spring_test.domain.product.Product;
import com.example.spring_test.domain.product.ProductRepository;
import com.example.spring_test.domain.product.ProductSellingStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductService {

	private final ProductRepository productRepository;

	public List<ProductResponse> getSellingProducts() {
		List<Product> products = productRepository.findAllBySellingStatusIn(
				ProductSellingStatus.forDisplay());

		return products.stream()
				.map(ProductResponse::of)
				.collect(Collectors.toList());
	}
}
