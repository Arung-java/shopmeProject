package com.amazon.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.RestController;

import com.amazon.admin.setting.country.CountryRepository;
import com.amazon.admin.setting.state.StateRepository;
import com.amazon.common.entity.Country;
import com.amazon.common.entity.State;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class StateRestControllerTests {
	
	@Autowired MockMvc mockMvc;
	
	@Autowired ObjectMapper objectMapper;
	
	@Autowired CountryRepository countryRepo;
	
	@Autowired StateRepository stateRepo;
	
	@Test
	@WithMockUser(username = "arun@mail.com", password = "nani1111", roles = "ADMIN")
	public void testListByCountries() throws Exception {
		Integer countryId=4;
		String url = "/states/list_by_country/"+countryId;
		MvcResult result = mockMvc.perform(get(url)).andExpect(status().isOk()).andDo(print()).andReturn();

		String jsonResponse = result.getResponse().getContentAsString();
		// System.out.println(jsonResponse);

		 State[] states = objectMapper.readValue(jsonResponse, State[].class);
		assertThat(states).hasSizeGreaterThan(0);
	}

	@Test
	@WithMockUser(username = "arun@mail.com", password = "nani1111", roles = "ADMIN")
	public void testCreateState() throws JsonProcessingException, Exception {
		String url = "/states/save";
		Integer countryId =4;
		Country country = countryRepo.findById(countryId).get();
		State state=new State("Telagana", country);
		MvcResult result = mockMvc.perform(post(url).contentType("application/json")
				.content(objectMapper.writeValueAsString(state)).with(csrf())).andDo(print())
				.andExpect(status().isOk()).andReturn();

		String reponse = result.getResponse().getContentAsString();
		Integer stateId = Integer.parseInt(reponse);
		Optional<State> findById = stateRepo.findById(stateId);
		assertThat(findById.isPresent());
		
		
	}
	
	@Test
	@WithMockUser(username = "arun@mail.com", password = "nani1111", roles = "ADMIN")
	public void testUpdateState() throws JsonProcessingException, Exception {
		String url = "/states/save";
		Integer stateId=5;
		String stateName = "Hyderabad";
		State state = stateRepo.findById(stateId).get();
		state.setName(stateName);
		mockMvc.perform(post(url).contentType("application/json")
				.content(objectMapper.writeValueAsString(state)).with(csrf())).andDo(print())
				.andExpect(status().isOk()).andExpect(content().string(String.valueOf(stateId)));

		
		Optional<State> findById = stateRepo.findById(stateId);
		assertThat(findById.isPresent());
		State updatedState = findById.get();
		assertThat(updatedState.getName()).isEqualTo(stateName);
	}
	
	@Test
	@WithMockUser(username = "arun@mail.com", password = "nani1111", roles = "ADMIN")
	public void testDeleteState() throws Exception {
		Integer stateId=3;
		String url ="/states/delete/"+stateId;
		mockMvc.perform(get(url)).andExpect(status().isOk());
		Optional<State> findById = stateRepo.findById(stateId);
		
		assertThat(findById).isNotPresent();
	}



	

}
