package com.amazon.admin.brand;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Rollback;

import com.amazon.admin.brands.BrandRepository;
import com.amazon.admin.brands.BrandService;
import com.amazon.common.entity.Brand;
import com.amazon.common.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class BrandRepositoryTests {
	@Autowired
	private BrandRepository repository;

	
	@Test
	public void testCreateBrand1() {
		Category category = new Category(5);
		Brand acer = new Brand("Acer");
		acer.getCategories().add(category);
		Brand savedBrand = repository.save(acer);

		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);

	}

	@Test
	public void testCreateBrand2() {
		Category category = new Category(7);
		Brand apple = new Brand("Apple");
		apple.getCategories().add(category);
		Brand savedBrand = repository.save(apple);

		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);

	}

	@Test
	public void testCreateBrand3() {

		Brand samsung = new Brand("Samsung");
		samsung.getCategories().add(new Category(29));// category memory
		samsung.getCategories().add(new Category(24));// category and hard drivve
		Brand savedBrand = repository.save(samsung);

		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);

	}

	@Test
	public void testFindAll() {
		Iterable<Brand> brands = repository.findAll();
		brands.forEach(System.out::println);

		assertThat(brands).isNotEmpty();
	}

	@Test
	public void testGetById() {
		Brand brand = repository.findById(1).get();
		System.out.println(brand);

		assertThat(brand.getName()).isEqualTo("Acer");
	}

	@Test
	public void testUpdateName() {
		String newName = "Samsung Electronics";
		Brand samsung = repository.findById(3).get();
		samsung.setName(newName);
		Brand savedBrand = repository.save(samsung);
		assertThat(savedBrand.getName()).isEqualTo(newName);
	}

	@Test
	public void testDelete() {
		Integer id = 2;
		repository.deleteById(id);

		Optional<Brand> result = repository.findById(id);
		assertThat(result).isEmpty();
	}

}
