package com.ecommerce.user.dto;

import com.ecommerce.user.model.Address;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressResponse {

	private Long id;
	private Long userId;
	private String street;
	private String city;
	private String state;
	private String postalCode;
	private String country;

	public static AddressResponse from(Address address) {
		return AddressResponse.builder()
				.id(address.getId())
				.userId(address.getUserId())
				.street(address.getStreet())
				.city(address.getCity())
				.state(address.getState())
				.postalCode(address.getPostalCode())
				.country(address.getCountry())
				.build();
	}
}
