package com.ecommerce.order.controller;

import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.dto.CartResponse;
import com.ecommerce.order.dto.QuantityRequest;
import com.ecommerce.order.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/{userId}/cart")
@RequiredArgsConstructor
public class CartController {

	private final CartService cartService;

	@GetMapping
	public ResponseEntity<CartResponse> getCart(@PathVariable Long userId) {
		return ResponseEntity.ok(cartService.getCart(userId));
	}

	@PostMapping("/items")
	public ResponseEntity<CartResponse> addItem(
			@PathVariable Long userId,
			@Valid @RequestBody CartItemRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addItem(userId, request));
	}

	@PutMapping("/items/{productId}")
	public ResponseEntity<CartResponse> updateItemQuantity(
			@PathVariable Long userId,
			@PathVariable Long productId,
			@Valid @RequestBody QuantityRequest request) {
		return ResponseEntity.ok(cartService.updateItemQuantity(userId, productId, request.getQuantity()));
	}

	@DeleteMapping("/items/{productId}")
	public ResponseEntity<CartResponse> removeItem(
			@PathVariable Long userId,
			@PathVariable Long productId) {
		return ResponseEntity.ok(cartService.removeItem(userId, productId));
	}

	@DeleteMapping
	public ResponseEntity<CartResponse> clearCart(@PathVariable Long userId) {
		return ResponseEntity.ok(cartService.clearCart(userId));
	}
}
