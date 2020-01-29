package com.majoapps.dogbreed.web;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.majoapps.dogbreed.business.domain.DogBreedResponse;
import com.majoapps.dogbreed.business.service.DogBreedService;
import com.majoapps.dogbreed.data.entity.DogBreed;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(DogServiceController.class)
@EnableAutoConfiguration(exclude = { JpaRepositoriesAutoConfiguration.class })
public class DogServiceControllerTest {
	private static final String BULLDOG = "Bulldog";

	@MockBean
	DogBreedService dogBreedService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void getAllDogBreedShouldReturnOk() throws Exception {
		String dogBreed = BULLDOG;
		List<String> dogBreeds = Arrays.asList(dogBreed);
		given(dogBreedService.getAllBreeds()).willReturn(dogBreeds);
		
		this.mockMvc.perform(get("/api/v1/dog")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString(dogBreed)));
	}
	
	@Test
	public void getAllDogBreedWithEmptyArrayShouldReturnNoContent() throws Exception {
		given(dogBreedService.getAllBreeds()).willReturn(new ArrayList<String>());
		this.mockMvc.perform(get("/api/v1/dog")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}
	
	@Test
	public void getAllDogBreedWithNullShouldReturnNoContent() throws Exception {
		given(dogBreedService.getAllBreeds()).willReturn(null);
		this.mockMvc.perform(get("/api/v1/dog")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}
	
	@Test
	public void getAllDogBreedShouldThrowException() throws Exception {
		given(dogBreedService.getAllBreeds()).willThrow(new NullPointerException());
		this.mockMvc.perform(get("/api/v1/dog")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}
	
	@Test
	public void getAllDogBreedsShouldReturnOk() throws Exception {
		String dogBreed = BULLDOG;
		DogBreedResponse dogBreedResponse = new DogBreedResponse();
		dogBreedResponse.setName(dogBreed);
		List<DogBreedResponse> dogBreeds = Arrays.asList(dogBreedResponse);
		given(dogBreedService.getAll()).willReturn(dogBreeds);
		
		this.mockMvc.perform(get("/api/v1/dog/all")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString(dogBreed)));
	}
	
	@Test
	public void getAllDogBreedsWithEmptyArrayShouldReturnNoContent() throws Exception {
		given(dogBreedService.getAll()).willReturn(new ArrayList<DogBreedResponse>());
		this.mockMvc.perform(get("/api/v1/dog/all")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}
	
	@Test
	public void getAllDogBreedsWithNullShouldReturnNoContent() throws Exception {
		given(dogBreedService.getAll()).willReturn(null);
		this.mockMvc.perform(get("/api/v1/dog/all")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}
	
	@Test
	public void getAllDogBreedsShouldThrowException() throws Exception {
		given(dogBreedService.getAll()).willThrow(new NullPointerException());
		this.mockMvc.perform(get("/api/v1/dog/all")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}
	
	@Test
	public void getDogBreedByIdShouldReturnOk() throws Exception {
		DogBreed dogBreed = new DogBreed();
		dogBreed.setName(BULLDOG);
		String uuid = "38400000-8cf0-11bd-b23e-10b96e4ef00d";
		UUID id = UUID.fromString(uuid);
		given(dogBreedService.getDogBreedById(id)).willReturn(dogBreed);
		this.mockMvc.perform(get("/api/v1/dog/" + uuid)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString(BULLDOG)));
	}
	
	@Test
	public void getByBreedNameShouldReturnOk() throws Exception {
		DogBreed bulldog = new DogBreed();
		bulldog.setName(BULLDOG);
		given(dogBreedService.getDogBreedByName(BULLDOG)).willReturn(Arrays.asList(bulldog));
		this.mockMvc.perform(get("/api/v1/dog/search?name=" + BULLDOG)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString(BULLDOG)));
	}
	
	@Test
	public void getByBreedNameWithEmtpyArrayShouldReturnNoContent() throws Exception {
		given(dogBreedService.getDogBreedByName(BULLDOG)).willReturn(new ArrayList<DogBreed>());
		this.mockMvc.perform(get("/api/v1/dog/search?name=" + BULLDOG)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}
	
	@Test
	public void getByBreedNameWithNullShouldReturnNoContent() throws Exception {
		given(dogBreedService.getDogBreedByName(BULLDOG)).willReturn(null);
		this.mockMvc.perform(get("/api/v1/dog/search?name=" + BULLDOG)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}
	
	@Test
	public void getByBreedNameShouldThrowException() throws Exception {
		given(dogBreedService.getDogBreedByName(BULLDOG)).willThrow(new NullPointerException());
		this.mockMvc.perform(get("/api/v1/dog/search?name=" + BULLDOG)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}
	
	@Test
	public void addRandomDogBreedShouldReturnOk() throws Exception {
		this.mockMvc.perform(post("/api/v1/dog/")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated());
	}
	
	@Test
	public void deleteDogByBreedIdShouldReturnOk() throws Exception {
		this.mockMvc.perform(delete("/api/v1/dog/38400000-8cf0-11bd-b23e-10b96e4ef00d")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}
}
