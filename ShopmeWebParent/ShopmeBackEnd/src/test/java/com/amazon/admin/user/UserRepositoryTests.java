package com.amazon.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.amazon.common.entity.Role;
import com.amazon.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	@Autowired
	private UserRepository repository;
	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateNewUserWithOneRole() {

		Role roleAdmin = entityManager.find(Role.class, 1);
		User userName = new User("arun@cgi.com", "jan2022", "arun", "gummula");
		userName.addRole(roleAdmin);
		User savedUser = repository.save(userName);

		assertThat(savedUser.getId()).isGreaterThan(0);

	}

	@Test
	public void testCreateNewUserWithTwoRoles() {

		Role roleAdmin = entityManager.find(Role.class, 1);
		User userRavi = new User("ravi@cts.com", "jan2022", "ravi", "kumar");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);
		userRavi.addRole(roleEditor);
		userRavi.addRole(roleAssistant);

		User savedUser = repository.save(userRavi);

		assertThat(savedUser.getId()).isGreaterThan(0);

	}

	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = repository.findAll();
		listUsers.forEach(user -> System.out.println(user));
	}

	@Test
	public void testGetUserById() {
		User user = repository.findById(2).get();
		System.out.println(user);
		assertThat(user).isNotNull();
	}

	@Test
	public void testUpdateUserDetalis() {
		User user = repository.findById(1).get();
		user.setEnabled(false);
		// user.setEmail("Ravikumar@Cts.com");
		repository.save(user);
		System.out.println(user);
		assertThat(user.getId()).isGreaterThan(0);
	}

	@Test
	public void testUpdateUserRoles() {
		User userRavi = repository.findById(2).get();
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(2);
		userRavi.getRoles().remove(roleEditor);
		userRavi.addRole(roleAssistant);
		User savedUser = repository.save(userRavi);

		assertThat(savedUser.getId()).isGreaterThan(0);

	}

	@Test
	public void testDeleteUser() {
		Integer userId = 2;
	repository.deleteById(userId);
	//assertThat(userId).isNull();
	}

	@Test
	public void testGetUserByEmail() {
		String email = "arun@cgi.com";
		User user = repository.getUserByEmail(email);
		assertThat(user).isNotNull();
	}

	@Test
	public void testCountById() {
		Integer id = 1;
		Long countById = repository.countById(id);
		assertThat(countById).isNotNull().isGreaterThan(0);

	}

	@Test
	public void testDisableUser() {
		Integer id = 9;
		repository.updateEnabledStatus(id, false);
	}

	@Test
	public void testEnabledUser() {
		Integer id = 9;
		repository.updateEnabledStatus(id, true);
	}

	@Test
	public void testListfirstpage() {
		int pageNumber = 2;
		int pageSize = 4;
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repository.findAll(pageable);
		List<User> listUsers = page.getContent();
		listUsers.forEach(user -> System.out.println(user));
		assertThat(listUsers.size()).isEqualTo(pageSize);

	}
	
	@Test
	public void testSearchUsers() {
		String keyword="bruce";
		int pageNumber = 0;
		int pageSize = 4;
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repository.findAll(keyword,pageable);
		List<User> listUsers = page.getContent();
		listUsers.forEach(user -> System.out.println(user));
		assertThat(listUsers.size()).isGreaterThan(0);
		
	}
}
