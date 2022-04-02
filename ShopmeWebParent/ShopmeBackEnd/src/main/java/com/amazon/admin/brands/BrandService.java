package com.amazon.admin.brands;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.amazon.admin.category.CategoryPageInfo;
import com.amazon.admin.paging.PagingAndSortingHelper;
import com.amazon.common.entity.Brand;
import com.amazon.common.entity.Category;

@Service
public class BrandService {

	public static final int BRANDS_PER_PAGE = 10;
	@Autowired
	private BrandRepository repository;

	public List<Brand> getAll() {
		return (List<Brand>) repository.findAll();
	}

	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
	helper.listEntites(pageNum, BRANDS_PER_PAGE, repository);	
	}

	public Brand save(Brand brand) {

		return repository.save(brand);
	}

	public Brand get(Integer id) throws BrandNotFoundException {
		try {

			return repository.findById(id).get();
		} catch (Exception e) {
			throw new BrandNotFoundException("Could not find any brand with ID " + id);
		}

	}

	public void delete(Integer id) throws BrandNotFoundException {
		Long countByID = repository.countById(id);

		if (countByID == null || countByID == 0) {
			throw new BrandNotFoundException("Could not find any brand with ID " + id);
		}

		repository.deleteById(id);
	}

	public String checkUnique(Integer id, String name) {
		boolean isCreatingNew = (id == null || id == 0);

		Brand brandByName = repository.findByName(name);

		if (isCreatingNew) {
			if (brandByName != null)
				return "Duplicate";
		} else {
			if (brandByName != null && brandByName.getId() != id) {
				return "Duplicate";
			}
		}

		return "OK";
	}

}
