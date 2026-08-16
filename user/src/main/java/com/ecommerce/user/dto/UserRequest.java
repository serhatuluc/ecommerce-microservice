package com.ecommerce.user.dto;

import com.ecommerce.user.model.Role;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Data;

@Data
public class UserRequest {

	@NotBlank(message = "First name is required")
	@Size(max = 100)
	private String firstName;

	@NotBlank(message = "Last name is required")
	@Size(max = 100)
	private String lastName;

	@NotBlank(message = "Email is required")
	@Email(message = "Email must be valid")
	@Size(max = 255)
	private String email;

	@NotBlank(message = "Password is required")
	@Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
	private String password;

	@Size(max = 30)
	private String phone;

	private Boolean active;

	private Role role;

	@Valid
	private List<AddressRequest> addresses;
}
