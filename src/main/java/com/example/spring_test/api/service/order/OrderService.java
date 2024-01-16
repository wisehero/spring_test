package com.example.spring_test.api.service.order;

import java.time.LocalDateTime;
import java.util.HashSet;
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
import com.example.spring_test.domain.product.ProductType;
import com.example.spring_test.domain.stock.Stock;
import com.example.spring_test.domain.stock.StockRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;
	private final StockRepository stockRepository;

	private static List<String> extractStockProductNumbers(List<Product> products) {
		return products.stream()
				.filter(product -> ProductType.containsStockType(product.getType()))
				.map(Product::getProductNumber)
				.collect(Collectors.toList());
	}

	private static Map<String, Long> createCountingMapBy(List<String> stockProductNumbers) {
		return stockProductNumbers.stream()
				.collect(Collectors.groupingBy(p -> p, Collectors.counting()));
	}

	public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime registeredDateTime) {
		List<String> productNumbers = request.getProductNumbers();
		List<Product> products = findProductBy(productNumbers);

		deductStockQuantities(products);

		Order order = Order.create(products, registeredDateTime);
		Order savedOrder = orderRepository.save(order);
		return OrderResponse.of(savedOrder);
	}

	private void deductStockQuantities(List<Product> products) {
		List<String> stockProductNumbers = extractStockProductNumbers(products);

		Map<String, Stock> stockMap = createStockMapBy(stockProductNumbers);

		// 상품별 counting
		Map<String, Long> productCountingMap = createCountingMapBy(stockProductNumbers);

		// 재고 차감 시도
		for (String stockProductNumber : new HashSet<>(stockProductNumbers)) {
			Stock stock = stockMap.get(stockProductNumber);
			int quantity = productCountingMap.get(stockProductNumber).intValue();

			if (stock.isQuantityLessThan(quantity)) {
				throw new IllegalArgumentException("재고가 부족한 상품이 있습니다.");
			}
			stock.deductQuantity(quantity);
		}
	}

	private Map<String, Stock> createStockMapBy(List<String> stockProductNumbers) {
		List<Stock> stocks = stockRepository.findAllByProductNumberIn(stockProductNumbers);
		return stocks.stream().collect(Collectors.toMap(Stock::getProductNumber, s -> s));
	}

	private List<Product> findProductBy(List<String> productNumbers) {
		List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);
		Map<String, Product> productMap = products.stream()
				.collect(Collectors.toMap(Product::getProductNumber, p -> p));

		return productNumbers.stream().map(productMap::get)
				.toList();
	}
}
