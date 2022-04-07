package com.amazon.admin.product;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amazon.admin.paging.PagingAndSortingHelper;
import com.amazon.common.entity.product.Product;
import com.amazon.common.exception.ProductNotFoundException;

@Service
@Transactional
public class ProductService {
	public static final int PRODUCTS_PER_PAGE = 5;
	@Autowired
	private ProductRepository repository;

	public List<Product> listAll() {
		return (List<Product>) repository.findAll();

	}

	public void listByPage(int pageNum, PagingAndSortingHelper helper, Integer categoryId) {
		

		Pageable pageable = helper.createPageable(PRODUCTS_PER_PAGE, pageNum);
		String keyword = helper.getKeyword();
		Page<Product> page=null;

		if (keyword != null && !keyword.isEmpty()) {
			if (categoryId != null && categoryId > 0) {
				String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
				page= repository.searchInCategory(categoryId, categoryIdMatch, keyword, pageable);
			}else {
				page= repository.findAll(keyword, pageable);
			}
		}else {

		if (categoryId != null && categoryId > 0) {
			String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
			page= repository.findAllInCategory(categoryId, categoryIdMatch, pageable);
		}else {
			page= repository.findAll(pageable);
		}
	}
	helper.updateModelAttributes(pageNum, page);	
	}
	
	public void searchProducts(int pageNum, PagingAndSortingHelper helper) {
		Pageable pageable = helper.createPageable(PRODUCTS_PER_PAGE, pageNum);
		String keyword = helper.getKeyword();
		
		Page<Product> page = repository.searchProductsByName(keyword, pageable);
		
		helper.updateModelAttributes(pageNum, page);
		
	}

	public Product save(Product product) {
		if (product.getId() == null) {
			product.setCreatedTime(new Date());
		}

		if (product.getAlias() == null || product.getAlias().isEmpty()) {
			String defaultAlias = product.getName().replaceAll(" ", "-");
			product.setAlias(defaultAlias);
		} else {
			product.setAlias(product.getAlias().replaceAll(" ", "-"));
		}

		product.setUpdatedTime(new Date());

		return repository.save(product);
	}

	public void saveProductPrice(Product productInForm) {
		Product productInDB = repository.findById(productInForm.getId()).get();
		productInDB.setCost(productInForm.getCost());
		productInDB.setPrice(productInForm.getPrice());
		productInDB.setDiscountPercent(productInForm.getDiscountPercent());

		repository.save(productInDB);
	}

	public void updateProductEnabledStatus(Integer id, boolean enabled) {
		repository.updateEnabledStatus(id, enabled);

	}

	public void delete(Integer id) throws ProductNotFoundException {
		Long countByID = repository.countById(id);

		if (countByID == null || countByID == 0) {
			throw new ProductNotFoundException("Could not find any product with ID " + id);
		}

		repository.deleteById(id);
	}

	public String checkUnique(Integer id, String name) {
		boolean isCreatingNew = (id == null || id == 0);

		Product productByName = repository.findByName(name);

		if (isCreatingNew) {
			if (productByName != null)
				return "Duplicate";
		} else {
			if (productByName != null && productByName.getId() != id) {
				return "Duplicate";
			}
		}

		return "OK";
	}

	public Product get(Integer id) throws ProductNotFoundException {
		try {
			return repository.findById(id).get(); // here get may throw the exception that's way we added in try block
		} catch (NoSuchElementException ex) {
			throw new ProductNotFoundException("Could not find any product with ID " + id);
		}
	}
}
