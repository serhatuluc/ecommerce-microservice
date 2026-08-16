package com.ecommerce.order.client;

import lombok.Data;

@Data
public class AddressDto {

	private Long id;
	private Long userId;
	private String street;
	private String city;
	private String state;
	private String postalCode;
	private String country;
}
