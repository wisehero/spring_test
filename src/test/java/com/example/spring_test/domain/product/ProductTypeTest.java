package com.example.spring_test.domain.product;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ProductTypeTest {

	@DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
	@Test
	void containsStockType() {
		// given
		ProductType givenType = ProductType.HANDMADE;

		// when
		boolean result = ProductType.containsStockType(givenType);

		// then
		assertThat(result).isFalse();
	}

	@DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
	@Test
	void containsStockType2() {
		// given
		ProductType givenType = ProductType.BAKERY;

		// when
		boolean result = ProductType.containsStockType(givenType);

		// then
		assertThat(result).isTrue();
	}

	@DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
	@CsvSource({"HANDMADE, false", "BOTTLE, true", "BAKERY, true"})
	@ParameterizedTest
	void containsStockType4(ProductType productType, boolean expected) {
		// when
		boolean result = ProductType.containsStockType(productType);

		//then
		assertThat(result).isEqualTo(expected);
	}



}
