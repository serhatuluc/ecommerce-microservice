package com.ecommerce.order.client;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ProductDto {

	private Long id;
	private String name;
	private String description;
	private String sku;
	private BigDecimal price;
	private Integer stockQuantity;
	private boolean active;
}
