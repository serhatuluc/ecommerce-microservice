package com.ecommerce.order.controller;

import com.ecommerce.order.dto.OrderRequest;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.service.OrderService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/{userId}/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@GetMapping
	public ResponseEntity<List<OrderResponse>> getAllOrders(@PathVariable Long userId) {
		return ResponseEntity.ok(orderService.findAllForUser(userId));
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<OrderResponse> getOrderById(
			@PathVariable Long userId,
			@PathVariable Long orderId) {
		return ResponseEntity.ok(orderService.findByIdForUser(userId, orderId));
	}

	@PostMapping
	public ResponseEntity<OrderResponse> placeOrder(
			@PathVariable Long userId,
			@Valid @RequestBody OrderRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(orderService.placeOrder(userId, request));
	}

	@PostMapping("/{orderId}/cancel")
	public ResponseEntity<OrderResponse> cancelOrder(
			@PathVariable Long userId,
			@PathVariable Long orderId) {
		return ResponseEntity.ok(orderService.cancelOrder(userId, orderId));
	}
}
