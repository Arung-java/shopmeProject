package com.amazon.admin.shippingrate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.amazon.admin.product.ProductRepository;
import com.amazon.common.entity.ShippingRate;
import com.amazon.common.entity.product.Product;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class ShippingRateServicesTests {
	@MockBean
	private ShippingRateRepositroy shipRepo;
	@MockBean
	private ProductRepository productRepo;

	@InjectMocks
	private ShippingRateService shipService;

	@Test
	public void testCalculateShippingCost_NoRateFound() {
		Integer productId = 1;
		Integer countryId = 234;
		String state = "ABCD";

		Mockito.when(shipRepo.findByCountryAndState(countryId, state)).thenReturn(null);

		assertThrows(ShippingRateNotFoundException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				shipService.calculateShippingCost(productId, countryId, state);
			}
		});
	}

	@Test
	public void testCalculateShippingCost_RateFound() throws ShippingRateNotFoundException {
		Integer productId = 1;
		Integer countryId = 234;
		String state = "New York";

		ShippingRate shippingRate = new ShippingRate();
		shippingRate.setRate(10);

		Mockito.when(shipRepo.findByCountryAndState(countryId, state)).thenReturn(shippingRate);
		Product product = new Product();
		product.setWeight(5);   // 4*3 =12  - 8*12=96 ->  96/139 = 0.69064.... -> so product is less then so it's 
		                             // returns product weight 5. -> then multiply by rate so 5*10 = 50
		product.setWidth(4);
		product.setHeight(3);
		product.setLength(8);

		Mockito.when(productRepo.findById(productId)).thenReturn(Optional.of(product));
		
		float calculateShippingCost = shipService.calculateShippingCost(productId, countryId, state);
		
		assertEquals(50, calculateShippingCost);
	}

}
