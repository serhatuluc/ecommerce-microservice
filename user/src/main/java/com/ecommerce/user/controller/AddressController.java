package com.ecommerce.user.controller;

import com.ecommerce.user.dto.AddressRequest;
import com.ecommerce.user.dto.AddressResponse;
import com.ecommerce.user.service.AddressService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/{userId}/addresses")
@RequiredArgsConstructor
public class AddressController {

	private final AddressService addressService;

	@GetMapping
	public ResponseEntity<List<AddressResponse>> getAllAddresses(@PathVariable Long userId) {
		return ResponseEntity.ok(addressService.findAllForUser(userId));
	}

	@GetMapping("/{addressId}")
	public ResponseEntity<AddressResponse> getAddressById(
			@PathVariable Long userId,
			@PathVariable Long addressId) {
		return ResponseEntity.ok(addressService.findByIdForUser(userId, addressId));
	}

	@PostMapping
	public ResponseEntity<AddressResponse> createAddress(
			@PathVariable Long userId,
			@Valid @RequestBody AddressRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(addressService.create(userId, request));
	}

	@PutMapping("/{addressId}")
	public ResponseEntity<AddressResponse> updateAddress(
			@PathVariable Long userId,
			@PathVariable Long addressId,
			@Valid @RequestBody AddressRequest request) {
		return ResponseEntity.ok(addressService.update(userId, addressId, request));
	}

	@DeleteMapping("/{addressId}")
	public ResponseEntity<Void> deleteAddress(
			@PathVariable Long userId,
			@PathVariable Long addressId) {
		addressService.delete(userId, addressId);
		return ResponseEntity.noContent().build();
	}
}
