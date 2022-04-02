package com.amazon.admin.setting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amazon.common.entity.Currency;

public interface CurrenyRepository extends CrudRepository<Currency, Integer> {

	public List<Currency> findAllByOrderByNameAsc();
}
