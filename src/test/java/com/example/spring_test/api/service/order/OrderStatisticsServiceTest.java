package com.example.spring_test.api.service.order;

import static com.example.spring_test.domain.product.ProductSellingStatus.*;
import static com.example.spring_test.domain.product.ProductType.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.spring_test.IntegrationTestSupport;
import com.example.spring_test.client.mail.MailSendClient;
import com.example.spring_test.domain.history.mail.MailSendHistory;
import com.example.spring_test.domain.history.mail.MailSendHistoryRepository;
import com.example.spring_test.domain.order.Order;
import com.example.spring_test.domain.order.OrderRepository;
import com.example.spring_test.domain.order.OrderStatus;
import com.example.spring_test.domain.orderproduct.OrderProductRepository;
import com.example.spring_test.domain.product.Product;
import com.example.spring_test.domain.product.ProductRepository;
import com.example.spring_test.domain.product.ProductType;

class OrderStatisticsServiceTest extends IntegrationTestSupport {

	@Autowired
	private OrderStatisticsService orderStatisticsService;
	@Autowired
	private OrderProductRepository orderProductRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private MailSendHistoryRepository mailSendHistoryRepository;


	@AfterEach
	void teardown() {
		orderProductRepository.deleteAllInBatch();
		orderRepository.deleteAllInBatch();
		productRepository.deleteAllInBatch();
		mailSendHistoryRepository.deleteAllInBatch();
	}

	@DisplayName("결제완료 주문들을 조회하여 매출 통계 메일을 전송한다.")
	@Test
	void sendOrderStatisticsMail() {
		// given
		LocalDateTime now = LocalDateTime.of(2023, 3, 5, 10, 0);

		Product product1 = createProduct(HANDMADE, "001", 1000);
		Product product2 = createProduct(HANDMADE, "002", 2000);
		Product product3 = createProduct(HANDMADE, "003", 3000);
		List<Product> products = List.of(product1, product2, product3);
		productRepository.saveAll(products);

		Order order1 = createPaymentCompletedOrder(LocalDateTime.of(2023, 3, 4, 23, 59, 59), products);
		Order order2 = createPaymentCompletedOrder(now, products);
		Order order3 = createPaymentCompletedOrder(LocalDateTime.of(2023, 3, 5, 23, 59, 59), products);
		Order order4 = createPaymentCompletedOrder(LocalDateTime.of(2023, 3, 6, 0, 0), products);

		// stubbing
		when(mailSendClient.sendEmail(any(String.class), any(String.class), any(String.class), any(String.class)))
				.thenReturn(true);

		// when
		boolean result = orderStatisticsService.SendOrderStatisticsMail(LocalDate.of(2023, 3, 5), "test@test.com");

		// then
		assertThat(result).isTrue();

		List<MailSendHistory> histories = mailSendHistoryRepository.findAll();
		assertThat(histories).hasSize(1)
				.extracting("content")
				.contains("총 매출 합계는 12000원입니다.");
	}

	private Order createPaymentCompletedOrder(LocalDateTime now, List<Product> products) {
		Order order = Order.builder()
				.products(products)
				.orderStatus(OrderStatus.PAYMENT_COMPLETED)
				.registeredDateTime(now)
				.build();
		return orderRepository.save(order);
	}

	private Product createProduct(ProductType type, String productNumber, int price) {
		return Product.builder()
				.type(type)
				.productNumber(productNumber)
				.price(price)
				.sellingStatus(SELLING)
				.name("메뉴 이름")
				.build();
	}

}
