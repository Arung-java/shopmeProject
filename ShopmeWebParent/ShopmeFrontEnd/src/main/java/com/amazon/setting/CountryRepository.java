package com.amazon.setting;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amazon.common.entity.Country;

public interface CountryRepository extends CrudRepository<Country, Integer> {
	public List<Country> findAllByOrderByNameAsc();
	@Query("SELECT c FROM Country c WHERE c.code = ?1")
	public Country findByCode(String code);

}
