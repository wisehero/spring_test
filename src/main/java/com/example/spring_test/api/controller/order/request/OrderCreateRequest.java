package com.example.spring_test.api.controller.order.request;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderCreateRequest {

	private List<String> productNumbers;

	@Builder
	private OrderCreateRequest(List<String> productNumbers) {
		this.productNumbers = productNumbers;
	}
}
