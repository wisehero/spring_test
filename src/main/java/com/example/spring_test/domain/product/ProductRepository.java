package com.example.spring_test.domain.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

	/**
	 * select *
	 * from product
	 * where selling_status in('SELLING', 'HOLD');
	 */
	List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> sellingStatuses);
}
