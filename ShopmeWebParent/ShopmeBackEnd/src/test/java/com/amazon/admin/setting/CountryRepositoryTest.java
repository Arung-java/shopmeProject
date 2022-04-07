package com.amazon.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.amazon.admin.setting.country.CountryRepository;
import com.amazon.common.entity.Country;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CountryRepositoryTest {
	@Autowired
	private CountryRepository repository;
	@Test
	public void testCreateCountry() {
		Country country = repository.save(new Country("China", "CN"));
		assertThat(country).isNotNull();
		assertThat(country.getId()).isGreaterThan(0);
	}
	@Test
	public void testListCountries() {
		List<Country> listCountries = repository.findAllByOrderByNameAsc();
		listCountries.forEach(System.out::println);
		assertThat(listCountries.size()).isGreaterThan(0);
	}
	@Test
	public void testUpdateCountry() {
		Integer id=1;
		String name="Republic of India";
		Country country = repository.findById(id).get();
		country.setName(name);
		
		Country updatedCountry = repository.save(country);
		assertThat(updatedCountry.getName()).isEqualTo(name);
	}
	@Test
	public void testGetCountry() {
		Integer id=1;
		Country country = repository.findById(id).get();
		assertThat(country).isNotNull();
	}
@Test
	public void testDeleteCountry() {
		Integer id=3;
		repository.deleteById(id);
		Optional<Country> findById = repository.findById(id);
		//assertThat(findById.isEmpty());
		
	}
}
