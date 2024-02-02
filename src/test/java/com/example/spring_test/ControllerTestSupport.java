package com.example.spring_test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.spring_test.api.controller.order.OrderController;
import com.example.spring_test.api.controller.product.ProductController;
import com.example.spring_test.api.service.order.OrderService;
import com.example.spring_test.api.service.product.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = {
		OrderController.class,
		ProductController.class
})
public abstract class ControllerTestSupport {

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;

	@MockBean
	protected OrderService orderService;

	@MockBean
	protected ProductService productService;
}
