package com.amazon.customer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.amazon.common.entity.AuthenticationType;
import com.amazon.common.entity.Country;
import com.amazon.common.entity.Customer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTests {
	
	@Autowired CustomerRepository repo;
	
	@Autowired private TestEntityManager entityManager;
	@Test
	public void testCreateCustomer1() {
		Integer countryId=234;//USA
		
		Country country=entityManager.find(Country.class, countryId);
		Customer customer=new Customer();
		customer.setCountry(country);
		customer.setFirstName("Arun");
		customer.setLastName("Gummula");
		customer.setPassword("Password123");
		customer.setEmail("arun@mail.com");
		customer.setPhoneNumber("123456789");
		customer.setAddressLine1("Bapu nagar");
		customer.setCity("HYD");
		customer.setState("Telangana");
		customer.setPostalCode("500038");
		customer.setCreatedTime(new Date());
		Customer savedCustomer = repo.save(customer);
		assertThat(savedCustomer).isNotNull();
		assertThat(savedCustomer.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateCustomer2() {
		Integer countryId=106;//India
		
		Country country=entityManager.find(Country.class, countryId);
		Customer customer=new Customer();
		customer.setCountry(country);
		customer.setFirstName("sumanth");
		customer.setLastName("juluru");
		customer.setPassword("Password111");
		customer.setEmail("sumanth@mail.com");
		customer.setPhoneNumber("9030135373");
		customer.setAddressLine1("7-1,Bapu nagar");
		customer.setAddressLine2("Near anupama clinc");
		customer.setCity("Srnagar");
		customer.setState("Telangana");
		customer.setPostalCode("500038");
		customer.setCreatedTime(new Date());
		Customer savedCustomer = repo.save(customer);
		assertThat(savedCustomer).isNotNull();
		assertThat(savedCustomer.getId()).isGreaterThan(0);
	}
	@Test
	public void testListCustomers() {
		Iterable<Customer> customers = repo.findAll();
		customers.forEach(System.out::println);
		assertThat(customers).hasSizeGreaterThan(1);
	}

	@Test
	public void testUpdateCustomer() {
		Integer customerId=2;
		String lastName="Messi";
		
		Customer customer = repo.findById(customerId).get();
		customer.setLastName(lastName);
		customer.setEnabled(true);
		
		Customer updatedCustomer = repo.save(customer);
		assertThat(updatedCustomer.getLastName()).isEqualTo(lastName);
	}
	@Test
	public void testGetCustomer() {
		Integer customerId=2;
		Optional<Customer> findById = repo.findById(customerId);
		assertThat(findById).isPresent();
		
		Customer customer = findById.get();
		System.out.println(customer);
		
	}
	
	@Test
	public void testDeleteCustomer() {
		Integer customerId=2;
		repo.deleteById(customerId);
		Optional<Customer> findById = repo.findById(customerId);
		assertThat(findById).isNotPresent();
		
	}
	@Test
	public void testFindByEmail() {
		String email="sumanth@mail.com";
		Customer customer = repo.findByEmail(email);
		assertThat(customer).isNotNull();
		System.out.println(customer);
	}
	@Test
	public void testFindByVerificationCode() {
		String code="code_123";
		Customer customer = repo.findByVerificationCode(code);
		assertThat(customer).isNotNull();
		System.out.println(customer);
	}
	@Test
	public void testEnableCustomer() {
		Integer customerId=1;
		repo.enable(customerId);
		Customer customer = repo.findById(customerId).get();
		assertThat(customer.isEnabled()).isTrue();
	}
@Test
	public void testUpdateAuthenticationType() {
	Integer id =1;
	repo.updateAuthenticationType(id, AuthenticationType.DATABASE);
	
	Customer customer = repo.findById(id).get();
	assertThat(customer.getAuthenticationType()).isEqualTo(AuthenticationType.DATABASE);
	
		
	}
}
