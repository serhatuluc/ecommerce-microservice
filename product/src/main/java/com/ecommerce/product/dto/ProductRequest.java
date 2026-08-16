package com.ecommerce.product.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class ProductRequest {

	@NotBlank(message = "Name is required")
	@Size(max = 150)
	private String name;

	@Size(max = 2000)
	private String description;

	@NotBlank(message = "Sku is required")
	@Size(max = 50)
	private String sku;

	@NotNull(message = "Price is required")
	@DecimalMin(value = "0.0", inclusive = true, message = "Price cannot be negative")
	private BigDecimal price;

	@NotNull(message = "Stock quantity is required")
	@Min(value = 0, message = "Stock quantity cannot be negative")
	private Integer stockQuantity;

	private Boolean active;
}
