package com.ecommerce.user.dto;

import com.ecommerce.user.model.Role;
import com.ecommerce.user.model.User;
import java.time.Instant;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {

	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private boolean active;
	private Role role;
	private List<AddressResponse> addresses;
	private Instant createdAt;
	private Instant updatedAt;

	public static UserResponse from(User user) {
		return UserResponse.builder()
				.id(user.getId())
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.email(user.getEmail())
				.phone(user.getPhone())
				.active(user.isActive())
				.role(user.getRole())
				.addresses(user.getAddresses() == null
						? List.of()
						: user.getAddresses().stream().map(AddressResponse::from).toList())
				.createdAt(user.getCreatedAt())
				.updatedAt(user.getUpdatedAt())
				.build();
	}
}
