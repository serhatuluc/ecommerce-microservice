package com.ecommerce.order.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

	@Bean
	RestClient userRestClient(@Value("${services.user.base-url}") String baseUrl) {
		return RestClient.builder().baseUrl(baseUrl).build();
	}

	@Bean
	RestClient productRestClient(@Value("${services.product.base-url}") String baseUrl) {
		return RestClient.builder().baseUrl(baseUrl).build();
	}
}
