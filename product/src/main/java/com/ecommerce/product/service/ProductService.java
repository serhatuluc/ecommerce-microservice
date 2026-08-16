package com.ecommerce.product.service;

import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.exception.DuplicateResourceException;
import com.ecommerce.product.exception.ResourceNotFoundException;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

	private final ProductRepository productRepository;

	@Transactional(readOnly = true)
	public List<ProductResponse> findAll() {
		return productRepository.findAll(Sort.by("id")).stream()
				.map(ProductResponse::from)
				.toList();
	}

	@Transactional(readOnly = true)
	public ProductResponse findById(Long id) {
		return ProductResponse.from(getProductOrThrow(id));
	}

	public ProductResponse create(ProductRequest request) {
		ensureSkuAvailable(request.getSku(), null);

		Product product = Product.builder()
				.name(request.getName())
				.description(request.getDescription())
				.sku(request.getSku())
				.price(request.getPrice())
				.stockQuantity(request.getStockQuantity())
				.active(Optional.ofNullable(request.getActive()).orElse(true))
				.build();

		return ProductResponse.from(productRepository.save(product));
	}

	public ProductResponse update(Long id, ProductRequest request) {
		Product product = getProductOrThrow(id);
		ensureSkuAvailable(request.getSku(), id);

		applyRequest(product, request, true);
		return ProductResponse.from(productRepository.saveAndFlush(product));
	}

	public ProductResponse patch(Long id, ProductRequest request) {
		Product product = getProductOrThrow(id);

		Optional.ofNullable(request.getSku())
				.ifPresent(sku -> ensureSkuAvailable(sku, id));

		applyRequest(product, request, false);
		return ProductResponse.from(productRepository.saveAndFlush(product));
	}

	public void delete(Long id) {
		if (!productRepository.existsById(id)) {
			throw new ResourceNotFoundException("Product not found with id: " + id);
		}
		productRepository.deleteById(id);
	}

	private Product getProductOrThrow(Long id) {
		return productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
	}

	private void ensureSkuAvailable(String sku, Long currentProductId) {
		boolean taken = currentProductId == null
				? productRepository.existsBySkuIgnoreCase(sku)
				: productRepository.existsBySkuIgnoreCaseAndIdNot(sku, currentProductId);

		if (taken) {
			throw new DuplicateResourceException("Sku already in use: " + sku);
		}
	}

	private void applyRequest(Product product, ProductRequest request, boolean replaceAll) {
		if (replaceAll || request.getName() != null) {
			product.setName(request.getName());
		}
		if (replaceAll || request.getDescription() != null) {
			product.setDescription(request.getDescription());
		}
		if (replaceAll || request.getSku() != null) {
			product.setSku(request.getSku());
		}
		if (replaceAll || request.getPrice() != null) {
			product.setPrice(request.getPrice());
		}
		if (replaceAll || request.getStockQuantity() != null) {
			product.setStockQuantity(request.getStockQuantity());
		}
		if (request.getActive() != null) {
			product.setActive(request.getActive());
		} else if (replaceAll) {
			product.setActive(true);
		}
	}
}
