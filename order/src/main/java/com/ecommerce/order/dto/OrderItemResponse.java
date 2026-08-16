package com.ecommerce.order.dto;

import com.ecommerce.order.model.OrderItem;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemResponse {

	private Long id;
	private Long productId;
	private String productName;
	private BigDecimal unitPrice;
	private Integer quantity;
	private BigDecimal lineTotal;

	public static OrderItemResponse from(OrderItem item) {
		return OrderItemResponse.builder()
				.id(item.getId())
				.productId(item.getProductId())
				.productName(item.getProductName())
				.unitPrice(item.getUnitPrice())
				.quantity(item.getQuantity())
				.lineTotal(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
				.build();
	}
}
