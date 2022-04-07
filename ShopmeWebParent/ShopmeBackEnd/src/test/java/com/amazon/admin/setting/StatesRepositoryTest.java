package com.amazon.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.amazon.admin.setting.state.StateRepository;
import com.amazon.common.entity.Country;
import com.amazon.common.entity.State;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class StatesRepositoryTest {
	@Autowired
	private StateRepository repo;
	@Autowired
	private TestEntityManager entityManager;
	@Test
	public void testCreateStatesInIndia() {
		Integer countryId=1;
		Country county = entityManager.find(Country.class, countryId);
		
		State state = repo.save(new State("Punjab", county));
		//State state = repo.save(new State("West Bengal", county));
		assertThat(state).isNotNull();
		assertThat(state.getId()).isGreaterThan(0);
	}
	@Test
	public void testCreateStatesInUS() {
		Integer countryId=1;
		Country county = entityManager.find(Country.class, countryId);
		
		State state = repo.save(new State("Texas", county));
		assertThat(state).isNotNull();
		assertThat(state.getId()).isGreaterThan(0);
		
	}
	@Test
	public void testListStatesByCountry() {
		Integer countryId=1;
		Country county = entityManager.find(Country.class, countryId);
	
		List<State> listStates = repo.findByCountryOrderByNameAsc(county);
		listStates.forEach(System.out::println);
		assertThat(listStates.size()).isGreaterThan(0);
		
	}
	@Test
	public void testUpdateState() {
		Integer stateId=1;
		String stateName="Tamil Nadu";
		State state = repo.findById(stateId).get();
		state.setName(stateName);
		State updatedState = repo.save(state);
		
		assertThat(updatedState.getName()).isEqualTo(stateName);
		 
	}
	@Test
	public void testGetState() {
		Integer stateId=1;
		Optional<State> findById = repo.findById(stateId);
		assertThat(findById.isPresent());
				
	}
	@Test
	public void testDeleteState() {
		Integer stateId=1;
		repo.deleteById(stateId);
		Optional<State> findById = repo.findById(stateId);
		//assertThat(findById.isEmpty());
	}

}
