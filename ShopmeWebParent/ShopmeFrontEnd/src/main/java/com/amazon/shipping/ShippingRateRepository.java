package com.amazon.shipping;

import org.springframework.data.repository.CrudRepository;

import com.amazon.common.entity.Country;
import com.amazon.common.entity.ShippingRate;

public interface ShippingRateRepository extends CrudRepository<ShippingRate, Integer> {

	public ShippingRate findByCountryAndState(Country country, String state);
}
