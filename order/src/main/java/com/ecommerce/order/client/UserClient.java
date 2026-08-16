package com.ecommerce.order.client;

import com.ecommerce.order.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class UserClient {

	private final RestClient userRestClient;

	public UserClient(@Qualifier("userRestClient") RestClient userRestClient) {
		this.userRestClient = userRestClient;
	}

	public UserDto getById(Long id) {
		return userRestClient.get()
				.uri("/api/users/{id}", id)
				.retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
					throw new ResourceNotFoundException("User not found with id: " + id);
				})
				.body(UserDto.class);
	}

	public AddressDto getAddress(Long userId, Long addressId) {
		return userRestClient.get()
				.uri("/api/users/{userId}/addresses/{addressId}", userId, addressId)
				.retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
					throw new ResourceNotFoundException(
							"Address not found with id: " + addressId + " for user: " + userId);
				})
				.body(AddressDto.class);
	}
}
