package com.amazon.admin.shippingrate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShippingRateRestController {

	@Autowired
	private ShippingRateService rateService;

	@PostMapping("/get_shipping_cost")
	public String getShippingCost(Integer productId, Integer countryId, String state)
			throws ShippingRateNotFoundException {
		float shippingCost = rateService.calculateShippingCost(productId, countryId, state);
		System.out.println(" shippingCost : " + shippingCost);
		return String.valueOf(shippingCost);
	}

}
