package com.ecommerce.order.dto;

import com.ecommerce.order.model.Cart;
import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartResponse {

	private Long id;
	private Long userId;
	private List<CartItemResponse> items;
	private BigDecimal totalPrice;

	public static CartResponse from(Cart cart) {
		List<CartItemResponse> itemResponses = cart.getItems().stream()
				.map(CartItemResponse::from)
				.toList();

		BigDecimal totalPrice = itemResponses.stream()
				.map(CartItemResponse::getLineTotal)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		return CartResponse.builder()
				.id(cart.getId())
				.userId(cart.getUserId())
				.items(itemResponses)
				.totalPrice(totalPrice)
				.build();
	}
}
