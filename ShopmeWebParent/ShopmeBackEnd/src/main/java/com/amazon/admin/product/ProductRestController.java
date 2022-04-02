package com.amazon.admin.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazon.common.entity.product.Product;
import com.amazon.common.exception.ProductNotFoundException;

@RestController
public class ProductRestController {
	@Autowired
	private ProductService service;

	@PostMapping("/products/check_unique")
	public String checkUnique(Integer id, String name) {//@Param("id") // @Param("name") Not requied this anation

		return service.checkUnique(id, name);

	}
	@GetMapping("/products/get/{id}")
	public ProductDTO getProductInfo(@PathVariable("id")Integer id) throws ProductNotFoundException {
		Product product = service.get(id);
		return new ProductDTO(product.getName(), product.getMainImagePath(), product.getDiscountPrice(), product.getCost());
		
	}
}
