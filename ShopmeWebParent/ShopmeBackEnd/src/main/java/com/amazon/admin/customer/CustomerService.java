package com.amazon.admin.customer;

import java.util.List;
import java.util.NoSuchElementException;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amazon.admin.paging.PagingAndSortingHelper;
import com.amazon.admin.setting.country.CountryRepository;
import com.amazon.common.entity.Country;
import com.amazon.common.entity.Customer;
import com.amazon.common.exception.CustomerNotFoundException;


@Service
@Transactional
public class CustomerService {
	
	public static final int CUSTOMERS_PER_PAGE = 10;
	
	@Autowired private CustomerRepository customerRepo;
	@Autowired private CountryRepository countryRepo;
	@Autowired private PasswordEncoder passwordEncoder;
	
	
	
	
	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		helper.listEntites(pageNum, CUSTOMERS_PER_PAGE, customerRepo);
		}


	public void save(Customer customerInForm) {
		Customer customerInDB = customerRepo.findById(customerInForm.getId()).get();
		
	if(!customerInForm.getPassword().isEmpty()) {
		String encodedPassword = passwordEncoder.encode(customerInForm.getPassword());
		customerInForm.setPassword(encodedPassword);
	}else {
		customerInForm.setPassword(customerInDB.getPassword());
	}
	 customerInForm.setEnabled(customerInDB.isEnabled());
	 customerInForm.setCreatedTime(customerInDB.getCreatedTime());
	 customerInForm.setVerificationCode(customerInDB.getVerificationCode());
	 customerInForm.setAuthenticationType(customerInDB.getAuthenticationType());
	 customerInForm.setResetPasswordToken(customerInDB.getResetPasswordToken());
	customerRepo.save(customerInForm);
	}

	
	public void updateCustomerEnabledStatus(Integer id, boolean enabled) {
		customerRepo.updateEnabledStatus(id, enabled);

	}
	public List<Country> listAllCountries(){
		return countryRepo.findAllByOrderByNameAsc();
		
	}

	public void delete(Integer id) throws CustomerNotFoundException {
		Long countByID = customerRepo.countById(id);

		if (countByID == null || countByID == 0) {
			throw new CustomerNotFoundException("Could not find any customers with ID " + id);
		}

		customerRepo.deleteById(id);
	}

	public boolean isEmailUnique(Integer id, String name) {
		Customer existCustomer = customerRepo.findByEmail(name);
		
		if(existCustomer !=null && existCustomer.getId() !=id) {
			//found another cuustomer having same email,
			return false;
		}
		
		return true;
		
	}

	public Customer get(Integer id) throws CustomerNotFoundException  {
		try {
			return customerRepo.findById(id).get(); // here get may throw the exception that's way we added in try block
		} catch (NoSuchElementException ex) {
			throw new CustomerNotFoundException("Could not find any cutomer with ID " + id);
		}
	}

	
}
