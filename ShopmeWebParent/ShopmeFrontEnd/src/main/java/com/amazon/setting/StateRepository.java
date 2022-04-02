package com.amazon.setting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amazon.common.entity.Country;
import com.amazon.common.entity.State;

public interface StateRepository extends CrudRepository<State, Integer> {
	public List<State> findByCountryOrderByNameAsc(Country country);

}
