package com.ecommerce.order.client;

import java.util.List;
import lombok.Data;

@Data
public class UserDto {

	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private boolean active;
	private List<AddressDto> addresses;
}
