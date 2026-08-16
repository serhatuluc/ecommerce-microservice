package com.ecommerce.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {

	private Long id;
	private Long userId;
	private String street;
	private String city;
	private String state;
	private String postalCode;
	private String country;
}
