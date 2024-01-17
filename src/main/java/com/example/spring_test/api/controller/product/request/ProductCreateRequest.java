package com.example.spring_test.api.controller.product.request;

import com.example.spring_test.domain.product.Product;
import com.example.spring_test.domain.product.ProductSellingStatus;
import com.example.spring_test.domain.product.ProductType;

import lombok.Builder;

public class ProductCreateRequest {
	private ProductType type;
	private ProductSellingStatus sellingStatus;
	private String name;
	private int price;

	@Builder
	private ProductCreateRequest(ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
		this.type = type;
		this.sellingStatus = sellingStatus;
		this.name = name;
		this.price = price;
	}

	public Product toEntity(String nextProductNumber) {
		return Product.builder()
				.productNumber(nextProductNumber)
				.type(type)
				.sellingStatus(sellingStatus)
				.name(name)
				.price(price)
				.build();
	}
}
