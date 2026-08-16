package com.ecommerce.order.service;

import com.ecommerce.order.client.ProductClient;
import com.ecommerce.order.client.ProductDto;
import com.ecommerce.order.client.UserClient;
import com.ecommerce.order.dto.OrderRequest;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.exception.EmptyCartException;
import com.ecommerce.order.exception.InsufficientStockException;
import com.ecommerce.order.exception.InvalidOrderStateException;
import com.ecommerce.order.exception.ResourceNotFoundException;
import com.ecommerce.order.model.Cart;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.model.OrderItem;
import com.ecommerce.order.model.OrderStatus;
import com.ecommerce.order.repository.CartRepository;
import com.ecommerce.order.repository.OrderRepository;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

	private final OrderRepository orderRepository;
	private final CartRepository cartRepository;
	private final UserClient userClient;
	private final ProductClient productClient;

	@Transactional(readOnly = true)
	public List<OrderResponse> findAllForUser(Long userId) {
		return orderRepository.findByUserId(userId).stream()
				.map(OrderResponse::from)
				.toList();
	}

	@Transactional(readOnly = true)
	public OrderResponse findByIdForUser(Long userId, Long orderId) {
		return OrderResponse.from(getOrderOrThrow(userId, orderId));
	}

	public OrderResponse placeOrder(Long userId, OrderRequest request) {
		userClient.getById(userId);
		userClient.getAddress(userId, request.getAddressId());

		Cart cart = cartRepository.findByUserId(userId).orElse(null);
		if (cart == null || cart.getItems().isEmpty()) {
			throw new EmptyCartException("Cannot place an order with an empty cart");
		}

		Order order = Order.builder()
				.userId(userId)
				.addressId(request.getAddressId())
				.build();

		BigDecimal total = BigDecimal.ZERO;
		for (CartItem cartItem : cart.getItems()) {
			ProductDto product = productClient.getById(cartItem.getProductId());
			int quantity = cartItem.getQuantity();

			if (quantity > product.getStockQuantity()) {
				throw new InsufficientStockException(
						"Only " + product.getStockQuantity() + " units of " + product.getName() + " are in stock");
			}

			productClient.updateStock(product.getId(), product.getStockQuantity() - quantity);

			order.getItems().add(OrderItem.builder()
					.order(order)
					.productId(product.getId())
					.productName(product.getName())
					.unitPrice(product.getPrice())
					.quantity(quantity)
					.build());

			total = total.add(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
		}
		order.setTotalPrice(total);

		Order savedOrder = orderRepository.save(order);

		cart.getItems().clear();
		cartRepository.save(cart);

		return OrderResponse.from(savedOrder);
	}

	public OrderResponse cancelOrder(Long userId, Long orderId) {
		Order order = getOrderOrThrow(userId, orderId);

		if (order.getStatus() == OrderStatus.CANCELLED) {
			throw new InvalidOrderStateException("Order is already cancelled");
		}

		for (OrderItem item : order.getItems()) {
			ProductDto product = productClient.getById(item.getProductId());
			productClient.updateStock(product.getId(), product.getStockQuantity() + item.getQuantity());
		}

		order.setStatus(OrderStatus.CANCELLED);
		return OrderResponse.from(orderRepository.saveAndFlush(order));
	}

	private Order getOrderOrThrow(Long userId, Long orderId) {
		return orderRepository.findByIdAndUserId(orderId, userId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Order not found with id: " + orderId + " for user: " + userId));
	}
}
