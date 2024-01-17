package com.example.spring_test.api.service.product;

import static com.example.spring_test.domain.product.ProductSellingStatus.*;
import static com.example.spring_test.domain.product.ProductType.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.spring_test.api.controller.product.request.ProductCreateRequest;
import com.example.spring_test.api.service.product.response.ProductResponse;
import com.example.spring_test.domain.product.Product;
import com.example.spring_test.domain.product.ProductRepository;
import com.example.spring_test.domain.product.ProductSellingStatus;
import com.example.spring_test.domain.product.ProductType;

@ActiveProfiles("test")
@SpringBootTest
class ProductServiceTest {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductRepository productRepository;

	@AfterEach
	void tearDown() {
		productRepository.deleteAllInBatch();
	}

	@DisplayName("신규 상품을 등록한다. 상품번호는 가장 최근 상품의 상품번호에서 1 증가한 값이다.")
	@Test
	void createProduct() {
		// given
		Product product = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
		productRepository.save(product);

		ProductCreateRequest request = ProductCreateRequest.builder()
				.type(HANDMADE)
				.sellingStatus(SELLING)
				.name("카푸치노")
				.price(5000)
				.build();

		// when
		ProductResponse productResponse = productService.createProduct(request);

		// then
		assertThat(productResponse)
				.extracting("productNumber", "type", "sellingStatus", "name", "price")
				.contains("002", HANDMADE, SELLING, "카푸치노", 5000);

		List<Product> products = productRepository.findAll();
		assertThat(products).hasSize(2)
				.extracting("productNumber", "type", "sellingStatus", "name", "price")
				.containsExactlyInAnyOrder(
						tuple("001", HANDMADE, SELLING, "아메리카노", 4000),
						tuple("002", HANDMADE, SELLING, "카푸치노", 5000)
				);
	}

	@DisplayName("상품이 하나도 없는 경우 신규 상품을 등록하면 상품번호는 001이다.")
	@Test
	void createProductWhenProductsIsEmpty() {
		// given
		ProductCreateRequest request = ProductCreateRequest.builder()
				.type(HANDMADE)
				.sellingStatus(SELLING)
				.name("카푸치노")
				.price(5000)
				.build();

		// when
		ProductResponse productResponse = productService.createProduct(request);

		// then
		assertThat(productResponse)
				.extracting("productNumber", "type", "sellingStatus", "name", "price")
				.contains("001", HANDMADE, SELLING, "카푸치노", 5000);

		List<Product> products = productRepository.findAll();
		assertThat(products).hasSize(1)
				.extracting("productNumber", "type", "sellingStatus", "name", "price")
				.contains(
						tuple("001", HANDMADE, SELLING, "카푸치노", 5000)
				);
	}

	private Product createProduct(String productNumber, ProductType type, ProductSellingStatus sellingStatus,
			String name, int price) {
		return Product.builder()
				.productNumber(productNumber)
				.type(type)
				.sellingStatus(sellingStatus)
				.name(name)
				.price(price)
				.build();
	}
}
