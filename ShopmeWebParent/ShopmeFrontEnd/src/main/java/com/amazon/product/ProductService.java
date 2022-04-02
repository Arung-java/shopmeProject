package com.amazon.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.amazon.common.entity.product.Product;
import com.amazon.common.exception.ProductNotFoundException;

@Service
public class ProductService {
	public static final int PRODUCTS_PER_PAGE = 10;
	public static final int SEARCH_RESULTS_PER_PAGE = 10;

	@Autowired
	private ProductRepository repository;

	public Page<Product> lisyByCategory(int pageNum, Integer categoryId) {
		String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";

		Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE);
		return repository.listByCategory(categoryId, categoryIdMatch, pageable);
	}

	public Product getProduct(String alise) throws ProductNotFoundException {
		Product product = repository.findByAlias(alise);
		if (product == null) {
			throw new ProductNotFoundException("Could not find any product with alias " + alise);
		}
		return product;
	}

	public Page<Product> search(String keyword, int pageNum) {
		Pageable pageable = PageRequest.of(pageNum - 1, SEARCH_RESULTS_PER_PAGE);
		return repository.search(keyword, pageable);
	}

}
