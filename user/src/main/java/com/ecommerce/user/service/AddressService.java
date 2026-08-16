package com.ecommerce.user.service;

import com.ecommerce.user.dto.AddressRequest;
import com.ecommerce.user.dto.AddressResponse;
import com.ecommerce.user.exception.ResourceNotFoundException;
import com.ecommerce.user.model.Address;
import com.ecommerce.user.model.User;
import com.ecommerce.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {

	private final UserRepository userRepository;
	private final SequenceGenerator sequenceGenerator;

	public List<AddressResponse> findAllForUser(Long userId) {
		return getUserOrThrow(userId).getAddresses().stream()
				.map(AddressResponse::from)
				.toList();
	}

	public AddressResponse findByIdForUser(Long userId, Long addressId) {
		return AddressResponse.from(getAddressOrThrow(getUserOrThrow(userId), addressId));
	}

	public AddressResponse create(Long userId, AddressRequest request) {
		User user = getUserOrThrow(userId);

		Address address = Address.builder()
				.id(sequenceGenerator.next("addresses"))
				.userId(userId)
				.street(request.getStreet())
				.city(request.getCity())
				.state(request.getState())
				.postalCode(request.getPostalCode())
				.country(request.getCountry())
				.build();

		user.getAddresses().add(address);
		userRepository.save(user);
		return AddressResponse.from(address);
	}

	public AddressResponse update(Long userId, Long addressId, AddressRequest request) {
		User user = getUserOrThrow(userId);
		Address address = getAddressOrThrow(user, addressId);

		address.setStreet(request.getStreet());
		address.setCity(request.getCity());
		address.setState(request.getState());
		address.setPostalCode(request.getPostalCode());
		address.setCountry(request.getCountry());

		userRepository.save(user);
		return AddressResponse.from(address);
	}

	public void delete(Long userId, Long addressId) {
		User user = getUserOrThrow(userId);
		Address address = getAddressOrThrow(user, addressId);
		user.getAddresses().remove(address);
		userRepository.save(user);
	}

	private User getUserOrThrow(Long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
	}

	private Address getAddressOrThrow(User user, Long addressId) {
		return user.getAddresses().stream()
				.filter(address -> addressId.equals(address.getId()))
				.findFirst()
				.orElseThrow(() -> new ResourceNotFoundException(
						"Address not found with id: " + addressId + " for user: " + user.getId()));
	}
}
