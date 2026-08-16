package com.ecommerce.order.dto;

import com.ecommerce.order.model.CartItem;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemResponse {

	private Long id;
	private Long productId;
	private String productName;
	private BigDecimal unitPrice;
	private Integer quantity;
	private BigDecimal lineTotal;

	public static CartItemResponse from(CartItem item) {
		BigDecimal unitPrice = item.getUnitPrice();
		return CartItemResponse.builder()
				.id(item.getId())
				.productId(item.getProductId())
				.productName(item.getProductName())
				.unitPrice(unitPrice)
				.quantity(item.getQuantity())
				.lineTotal(unitPrice.multiply(BigDecimal.valueOf(item.getQuantity())))
				.build();
	}
}
