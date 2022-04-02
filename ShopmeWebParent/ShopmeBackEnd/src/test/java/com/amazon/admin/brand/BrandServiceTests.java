package com.amazon.admin.brand;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.amazon.admin.brands.BrandRepository;
import com.amazon.admin.brands.BrandService;
import com.amazon.common.entity.Brand;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class BrandServiceTests {
	@MockBean
	private BrandRepository repository;
	@InjectMocks
	private BrandService brandService;

	@Test
	public void testUniqueInNewModeRuturnDuplicate() {
		Integer id = null;
		String name = "Acer";
		Brand brand = new Brand(name);
		Mockito.when(repository.findByName(name)).thenReturn(brand);
		String result = brandService.checkUnique(id, name);
		assertThat(result).isEqualTo("Duplicate");
	}

	@Test
	public void testUniqueInNewModeRuturnOK() {
		Integer id = null;
		String name = "AMD";
		// Brand brand = new Brand(name);
		Mockito.when(repository.findByName(name)).thenReturn(null);
		String result = brandService.checkUnique(id, name);
		assertThat(result).isEqualTo("OK");
	}

	@Test
	public void testUniqueInEditModeRuturnDuplicate() {
		Integer id = 1;
		String name = "Canon";
		Brand brand = new Brand(id, name);
		Mockito.when(repository.findByName(name)).thenReturn(brand);
		String result = brandService.checkUnique(2, "Canon");
		assertThat(result).isEqualTo("Duplicate");
	}

	@Test
	public void testUniqueInEditModeRuturnOK() {
		Integer id = 1;
		String name = "Canon";
		Brand brand = new Brand(id, name);
		Mockito.when(repository.findByName(name)).thenReturn(brand);
		String result = brandService.checkUnique(1, "Canon");
		assertThat(result).isEqualTo("OK");
	}

}
