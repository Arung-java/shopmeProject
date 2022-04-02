package com.amazon.admin.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.amazon.common.entity.Brand;
import com.amazon.common.entity.Category;
import com.amazon.common.entity.product.Product;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProdcuctRepositoryTests {
	@Autowired
	private ProductRepository repository;
	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateProduct() {

		Brand brand = entityManager.find(Brand.class, 37);
		Category category = entityManager.find(Category.class, 5);

		Product product = new Product();
		product.setName("Acer Aspire Desktop");
		product.setAlias("acer_aspire_desktop");
		product.setShortDescription("Short Description for Acer Aspire Desktop ");
		product.setFullDescription("This is a very good desktop  Acer Aspire Desktop full descriptiion");
		product.setBrand(brand);
		product.setCategory(category);

		product.setPrice(678);
		product.setCost(600);
		product.setEnabled(true);
		product.setInStock(true);
		product.setCreatedTime(new Date());
		product.setUpdatedTime(new Date());

		Product savedProduct = repository.save(product);
		assertThat(savedProduct).isNotNull();
		assertThat(savedProduct.getId()).isGreaterThan(0);

	}

	@Test
	public void testListAllProducts() {
		Iterable<Product> iterableProducts = repository.findAll();
		iterableProducts.forEach(System.out::println);

	}

	@Test
	public void testGetProduct() {
		Integer id = 2;
		Product product = repository.findById(id).get();
		System.out.println(product);
		assertThat(product).isNotNull();
	}

	@Test
	public void testUpdateProduct() {
		Integer id = 1;
		Product product = repository.findById(id).get();
		product.setPrice(599);
		repository.save(product);

		Product updateProduct = entityManager.find(Product.class, id);

		assertThat(updateProduct.getPrice()).isEqualTo(599);

	}

	@Test
	public void testDeleteProduct() {
		Integer id = 3;
		repository.deleteById(id);

		Optional<Product> result = repository.findById(id);

		assertThat(!result.isPresent());

	}

	@Test
	public void testSaveProductWithImages() {
		Integer productId = 1;
		Product product = repository.findById(productId).get();
		product.setMainImage("main image.jpg");
		product.addExtraImage("extra image1.png");
		product.addExtraImage("extra_image2.png");
		product.addExtraImage("extra_image3.png");
		Product savedProduct = repository.save(product);
		assertThat(savedProduct.getImages()).size().isEqualTo(3);
	}

	@Test
	public void testSaveProductWithDetalis() {
		Integer id = 1;
		Product product = repository.findById(id).get();
		product.addDetali("Device image", "128GB");
		product.addDetali("CPU Model", "MediaTek");
		product.addDetali("OS", "Android 10");

		Product savedProduct = repository.save(product);
		assertThat(savedProduct.getDetails()).isNotEmpty();

	}

}
