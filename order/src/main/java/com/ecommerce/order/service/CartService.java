package com.ecommerce.order.service;

import com.ecommerce.order.client.ProductClient;
import com.ecommerce.order.client.ProductDto;
import com.ecommerce.order.client.UserClient;
import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.dto.CartResponse;
import com.ecommerce.order.exception.InsufficientStockException;
import com.ecommerce.order.exception.ResourceNotFoundException;
import com.ecommerce.order.model.Cart;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.repository.CartRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

	private final CartRepository cartRepository;
	private final UserClient userClient;
	private final ProductClient productClient;

	@Transactional(readOnly = true)
	public CartResponse getCart(Long userId) {
		return CartResponse.from(getOrCreateCart(userId));
	}

	public CartResponse addItem(Long userId, CartItemRequest request) {
		Cart cart = getOrCreateCart(userId);
		ProductDto product = productClient.getById(request.getProductId());

		Optional<CartItem> existingItem = findItem(cart, request.getProductId());
		int newQuantity = existingItem.map(item -> item.getQuantity() + request.getQuantity())
				.orElse(request.getQuantity());
		ensureInStock(product, newQuantity);

		if (existingItem.isPresent()) {
			CartItem item = existingItem.get();
			item.setQuantity(newQuantity);
			item.setProductName(product.getName());
			item.setUnitPrice(product.getPrice());
		} else {
			cart.getItems().add(CartItem.builder()
					.cart(cart)
					.productId(product.getId())
					.productName(product.getName())
					.unitPrice(product.getPrice())
					.quantity(request.getQuantity())
					.build());
		}

		return CartResponse.from(cartRepository.saveAndFlush(cart));
	}

	public CartResponse updateItemQuantity(Long userId, Long productId, Integer quantity) {
		Cart cart = getOrCreateCart(userId);
		CartItem item = getItemOrThrow(cart, productId);
		ProductDto product = productClient.getById(productId);

		ensureInStock(product, quantity);
		item.setQuantity(quantity);
		item.setProductName(product.getName());
		item.setUnitPrice(product.getPrice());

		return CartResponse.from(cartRepository.saveAndFlush(cart));
	}

	public CartResponse removeItem(Long userId, Long productId) {
		Cart cart = getOrCreateCart(userId);
		CartItem item = getItemOrThrow(cart, productId);

		cart.getItems().remove(item);

		return CartResponse.from(cartRepository.saveAndFlush(cart));
	}

	public CartResponse clearCart(Long userId) {
		Cart cart = getOrCreateCart(userId);
		cart.getItems().clear();

		return CartResponse.from(cartRepository.saveAndFlush(cart));
	}

	Cart getOrCreateCart(Long userId) {
		return cartRepository.findByUserId(userId)
				.orElseGet(() -> {
					userClient.getById(userId);
					return cartRepository.save(Cart.builder().userId(userId).build());
				});
	}

	private Optional<CartItem> findItem(Cart cart, Long productId) {
		return cart.getItems().stream()
				.filter(item -> item.getProductId().equals(productId))
				.findFirst();
	}

	private CartItem getItemOrThrow(Cart cart, Long productId) {
		return findItem(cart, productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found in cart: " + productId));
	}

	private void ensureInStock(ProductDto product, int quantity) {
		if (quantity > product.getStockQuantity()) {
			throw new InsufficientStockException(
					"Only " + product.getStockQuantity() + " units of " + product.getName() + " are in stock");
		}
	}
}
