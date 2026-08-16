package com.ecommerce.order.dto;

import com.ecommerce.order.model.Order;
import com.ecommerce.order.model.OrderStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponse {

	private Long id;
	private Long userId;
	private Long addressId;
	private OrderStatus status;
	private List<OrderItemResponse> items;
	private BigDecimal totalPrice;
	private Instant createdAt;
	private Instant updatedAt;

	public static OrderResponse from(Order order) {
		return OrderResponse.builder()
				.id(order.getId())
				.userId(order.getUserId())
				.addressId(order.getAddressId())
				.status(order.getStatus())
				.items(order.getItems().stream().map(OrderItemResponse::from).toList())
				.totalPrice(order.getTotalPrice())
				.createdAt(order.getCreatedAt())
				.updatedAt(order.getUpdatedAt())
				.build();
	}
}
