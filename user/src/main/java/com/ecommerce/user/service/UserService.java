package com.ecommerce.user.service;

import com.ecommerce.user.dto.AddressRequest;
import com.ecommerce.user.dto.UserRequest;
import com.ecommerce.user.dto.UserResponse;
import com.ecommerce.user.exception.DuplicateResourceException;
import com.ecommerce.user.exception.ResourceNotFoundException;
import com.ecommerce.user.model.Address;
import com.ecommerce.user.model.Role;
import com.ecommerce.user.model.User;
import com.ecommerce.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final SequenceGenerator sequenceGenerator;

	public List<UserResponse> findAll() {
		return userRepository.findAll(Sort.by("id")).stream()
				.map(UserResponse::from)
				.toList();
	}

	public UserResponse findById(Long id) {
		return UserResponse.from(getUserOrThrow(id));
	}

	public UserResponse create(UserRequest request) {
		ensureEmailAvailable(request.getEmail(), null);

		User user = User.builder()
				.id(sequenceGenerator.next("users"))
				.firstName(request.getFirstName())
				.lastName(request.getLastName())
				.email(request.getEmail())
				.password(request.getPassword())
				.phone(request.getPhone())
				.active(Optional.ofNullable(request.getActive()).orElse(true))
				.role(Optional.ofNullable(request.getRole()).orElse(Role.CUSTOMER))
				.build();

		replaceAddresses(user, request.getAddresses());

		return UserResponse.from(userRepository.save(user));
	}

	public UserResponse update(Long id, UserRequest request) {
		User user = getUserOrThrow(id);
		ensureEmailAvailable(request.getEmail(), id);

		applyRequest(user, request, true);
		replaceAddresses(user, request.getAddresses());
		return UserResponse.from(userRepository.save(user));
	}

	public UserResponse patch(Long id, UserRequest request) {
		User user = getUserOrThrow(id);

		Optional.ofNullable(request.getEmail())
				.ifPresent(email -> ensureEmailAvailable(email, id));

		applyRequest(user, request, false);
		replaceAddresses(user, request.getAddresses());
		return UserResponse.from(userRepository.save(user));
	}

	public void delete(Long id) {
		if (!userRepository.existsById(id)) {
			throw new ResourceNotFoundException("User not found with id: " + id);
		}
		userRepository.deleteById(id);
	}

	private User getUserOrThrow(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
	}

	private void ensureEmailAvailable(String email, Long currentUserId) {
		boolean taken = currentUserId == null
				? userRepository.existsByEmailIgnoreCase(email)
				: userRepository.existsByEmailIgnoreCaseAndIdNot(email, currentUserId);

		if (taken) {
			throw new DuplicateResourceException("Email already in use: " + email);
		}
	}

	private void applyRequest(User user, UserRequest request, boolean replaceAll) {
		if (replaceAll || request.getFirstName() != null) {
			user.setFirstName(request.getFirstName());
		}
		if (replaceAll || request.getLastName() != null) {
			user.setLastName(request.getLastName());
		}
		if (replaceAll || request.getEmail() != null) {
			user.setEmail(request.getEmail());
		}
		if (replaceAll || request.getPassword() != null) {
			user.setPassword(request.getPassword());
		}
		if (replaceAll || request.getPhone() != null) {
			user.setPhone(request.getPhone());
		}
		if (request.getActive() != null) {
			user.setActive(request.getActive());
		} else if (replaceAll) {
			user.setActive(true);
		}
		if (request.getRole() != null) {
			user.setRole(request.getRole());
		} else if (replaceAll) {
			user.setRole(Role.CUSTOMER);
		}
	}

	private void replaceAddresses(User user, List<AddressRequest> addressRequests) {
		if (addressRequests == null) {
			return;
		}

		if (user.getAddresses() == null) {
			user.setAddresses(new ArrayList<>());
		}
		user.getAddresses().clear();
		addressRequests.stream()
				.map(addressRequest -> Address.builder()
						.id(sequenceGenerator.next("addresses"))
						.userId(user.getId())
						.street(addressRequest.getStreet())
						.city(addressRequest.getCity())
						.state(addressRequest.getState())
						.postalCode(addressRequest.getPostalCode())
						.country(addressRequest.getCountry())
						.build())
				.forEach(user.getAddresses()::add);
	}
}
