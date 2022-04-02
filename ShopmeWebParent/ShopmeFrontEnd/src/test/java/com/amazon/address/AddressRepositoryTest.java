package com.amazon.address;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.amazon.common.entity.Address;
import com.amazon.common.entity.Country;
import com.amazon.common.entity.Customer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class AddressRepositoryTest {
	
	@Autowired private AddressRepository repo;
	@Test
	public void testAddNew() {
		Integer customerId=37;
		Integer countryId=234;
		
		Address newAddress= new Address();
		newAddress.setCustomer(new Customer(customerId));
		newAddress.setCountry(new Country(countryId));
		newAddress.setFirstName("Arun");
		newAddress.setLastName("Nani");
		newAddress.setPhoneNumber("9939898797");
		newAddress.setAddressLine1("RGM");
		newAddress.setCity("GDK");
		newAddress.setState("Telagana");
		newAddress.setPostalCode("505208");
		Address savedAddress = repo.save(newAddress);
		assertThat(savedAddress).isNotNull();
		assertThat(savedAddress.getId()).isGreaterThan(0);
		
	}
	@Test
	public void testFindByCustomer() {
		Integer customerId=5;
		List<Address> listAddresses = repo.findByCustomer(new Customer(customerId));
		assertThat(listAddresses.size()).isGreaterThan(0);
		listAddresses.forEach(System.out::println);
	}
	@Test
	public void testFindByIdAndCustomer() {
		Integer addressId=3;
		Integer customerId= 5;
		Address address = repo.findByIdAndCustomer(addressId, customerId);
		assertThat(address).isNotNull();
		System.out.println(address);
		
	}
	@Test
	public void testUpdate() {
		Integer addressId=3;
	//	String phoneNumer= "9949750018";
		
		Address address=repo.findById(addressId).get();
		//address.setPhoneNumber(phoneNumer);
		address.setDefaultForShipping(true);
		Address updatedAddress = repo.save(address);
		//assertThat(updatedAddress.getPhoneNumber()).isEqualTo(phoneNumer);
	}
	
	@Test
	public void testDeleteByIdAndCustomer() {
		Integer addressId=1;
		Integer customerId= 234;
		repo.deleteByIdAndCustomer(addressId, customerId);
		Address address = repo.findByIdAndCustomer(addressId, customerId);
		assertThat(address).isNull();
		
	}
	@Test
	public void testSetDefault() {
		Integer addressId=8;
		repo.setDefaultAddress(addressId);
		Address address = repo.findById(addressId).get();
		assertThat(address.isDefaultForShipping()).isTrue();
	}
	@Test
	public void setNonDefaultAddresses() {
		Integer addressId=8;
		Integer customerId=37;
		repo.setNonDefaultForOthers(addressId, customerId);
		
	}
	@Test
	public void testGetDefault() {
		Integer customerId=43;
		Address address = repo.findDefaultByCustomer(customerId);
		assertThat(address).isNotNull();
	}

}
