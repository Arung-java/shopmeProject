package com.amazon.admin.product;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.amazon.common.entity.Brand;
import com.amazon.common.entity.product.Product;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@MockBean
	private ProductRepository repository;
	@InjectMocks
	private ProductService service;

	@Test
	public void testUniqueInNewModeRuturnDuplicate() {
		Integer id = null;
		String name = "Dell Inspiron";
		Product product = new Product();
		product.setName(name);
		Mockito.when(repository.findByName(name)).thenReturn(product);
		String result = service.checkUnique(id, name);
		assertThat(result).isEqualTo("Duplicate");
	}
	
	@Test
	public void testUniqueInNewModeRuturnOK() {
		Integer id = null;
		String name = "Dell Inspiron";
//		Product product = new Product();
//		product.setName(name);
		Mockito.when(repository.findByName(name)).thenReturn(null);
		String result = service.checkUnique(id, name);
		assertThat(result).isEqualTo("OK");
	}
	@Test
	public void testUniqueInEditModeRuturnDuplicate() {
		Integer id = 1;
		String name = "Dell Inspiron";
		Product product = new Product();
		product.setName(name);
		Mockito.when(repository.findByName(name)).thenReturn(product);
		String result = service.checkUnique(2, name);
		assertThat(result).isEqualTo("Duplicate");
	}
	
	@Test
	public void testUniqueInEditModeRuturnOK() {
		Integer id = 1;
		String name = "Dell Inspiron";
		Product product = new Product();
		product.setName(name);
		Mockito.when(repository.findByName(name)).thenReturn(null);
		String result = service.checkUnique(1, name);
		assertThat(result).isEqualTo("OK");
	}
	

}
