package com.ecommerce.order.client;

import com.ecommerce.order.exception.ResourceNotFoundException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ProductClient {

	private final RestClient productRestClient;

	public ProductClient(@Qualifier("productRestClient") RestClient productRestClient) {
		this.productRestClient = productRestClient;
	}

	public ProductDto getById(Long id) {
		return productRestClient.get()
				.uri("/api/products/{id}", id)
				.retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
					throw new ResourceNotFoundException("Product not found with id: " + id);
				})
				.body(ProductDto.class);
	}

	public void updateStock(Long id, int stockQuantity) {
		productRestClient.patch()
				.uri("/api/products/{id}", id)
				.body(Map.of("stockQuantity", stockQuantity))
				.retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
					throw new ResourceNotFoundException("Product not found with id: " + id);
				})
				.toBodilessEntity();
	}
}
