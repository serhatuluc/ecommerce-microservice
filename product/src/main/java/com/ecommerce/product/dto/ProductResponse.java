package com.ecommerce.product.dto;

import com.ecommerce.product.model.Product;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {

	private Long id;
	private String name;
	private String description;
	private String sku;
	private BigDecimal price;
	private Integer stockQuantity;
	private boolean active;
	private Instant createdAt;
	private Instant updatedAt;

	public static ProductResponse from(Product product) {
		return ProductResponse.builder()
				.id(product.getId())
				.name(product.getName())
				.description(product.getDescription())
				.sku(product.getSku())
				.price(product.getPrice())
				.stockQuantity(product.getStockQuantity())
				.active(product.isActive())
				.createdAt(product.getCreatedAt())
				.updatedAt(product.getUpdatedAt())
				.build();
	}
}
