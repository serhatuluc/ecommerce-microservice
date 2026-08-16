package com.ecommerce.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderRequest {

	@NotNull(message = "Address id is required")
	private Long addressId;
}
