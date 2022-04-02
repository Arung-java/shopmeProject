package com.amazon.admin.shippingrate;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazon.admin.paging.PagingAndSortingHelper;
import com.amazon.admin.product.ProductRepository;
import com.amazon.admin.setting.country.CountryRepository;
import com.amazon.common.entity.Country;
import com.amazon.common.entity.ShippingRate;
import com.amazon.common.entity.product.Product;

@Service
@Transactional
public class ShippingRateService {
	private static final int DIM_DIVISOR = 139;
	public static final int RATES_PER_PAGE = 10;

	@Autowired
	private ShippingRateRepositroy shipRepo;
	@Autowired
	private CountryRepository countryRepo;
	@Autowired
	private ProductRepository productRepo;

	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		helper.listEntites(pageNum, RATES_PER_PAGE, shipRepo);
	}

	public List<Country> listAllCountries() {
		return countryRepo.findAllByOrderByNameAsc();
	}

	public void save(ShippingRate rateInForm) throws ShippingRateAlreadyExistsException {
		ShippingRate rateInDB = shipRepo.findByCountryAndState(rateInForm.getCountry().getId(), rateInForm.getState());
		boolean foundExistingRateInNewMode = rateInForm.getId() == null && rateInDB != null;
		boolean foundDifferentExistingRateInEditMode = rateInForm.getId() != null && rateInDB != null
				&& !rateInDB.equals(rateInForm);

		if (foundExistingRateInNewMode || foundDifferentExistingRateInEditMode) {
			throw new ShippingRateAlreadyExistsException("There's  already a rate for the destination"
					+ rateInForm.getCountry().getName() + "," + rateInForm.getState());
		}
		shipRepo.save(rateInForm);
	}

	public ShippingRate get(Integer id) throws ShippingRateNotFoundException {
		try {
			return shipRepo.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new ShippingRateNotFoundException("Could not find shipping rate with ID " + id);
		}
	}

	public void updateCODSupport(Integer id, boolean codSupported) throws ShippingRateNotFoundException {

		Long count = shipRepo.countById(id);
		if (count == null || count == 0) {
			throw new ShippingRateNotFoundException("Could not find shipping rate with ID " + id);
		}
		shipRepo.updateCODSupport(id, codSupported);
	}

	public void delete(Integer id) throws ShippingRateNotFoundException {
		Long count = shipRepo.countById(id);
		if (count == null || count == 0) {
			throw new ShippingRateNotFoundException("Could not find shipping rate with ID " + id);
		}
		shipRepo.deleteById(id);
	}

	public float calculateShippingCost(Integer productId, Integer countryId, String state)
			throws ShippingRateNotFoundException {
		ShippingRate shippingRate = shipRepo.findByCountryAndState(countryId, state);
		if (shippingRate == null) {
			throw new ShippingRateNotFoundException(
					"No Shipping rate found for the given " + "destination. You have to enter shipping cost manually.");
		}
		Product product = productRepo.findById(productId).get();

		float dimWeight = (product.getLength() * product.getWidth() * product.getHeight()) / DIM_DIVISOR;
System.out.println("dimWeight : " + dimWeight);
		float finalWeight = product.getWeight() > dimWeight ? product.getWeight() : dimWeight;
		System.out.println("finalWeight : " + finalWeight);
		System.out.println(" FInal :  " + finalWeight * shippingRate.getRate());
		
		return finalWeight * shippingRate.getRate();
	}
}
