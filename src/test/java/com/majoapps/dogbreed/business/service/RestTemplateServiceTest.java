package com.majoapps.dogbreed.business.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import com.majoapps.dogbreed.business.domain.DogBreedDto;

@RunWith(MockitoJUnitRunner.class)
public class RestTemplateServiceTest {
	@Mock
	private RestTemplate restTemplate;
	
	@InjectMocks
	private RestTemplateService restTemplateService;
	
	@BeforeEach
	void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	@DisplayName("Get Random Dog")
	public void getRandomDog() throws Exception {
		String testMessage = "test message";
		String testStatus = "test status";
		DogBreedDto dogBreedWrapper = new DogBreedDto();
		dogBreedWrapper.setMessage(testMessage);
		dogBreedWrapper.setStatus(testStatus);
		when(restTemplate.getForObject("https://dog.ceo/api/breeds/image/random", DogBreedDto.class))
			.thenReturn(dogBreedWrapper);
		
		DogBreedDto randomDog = restTemplateService.getRandomDog();
		assertEquals(testMessage, randomDog.getMessage());
		assertEquals(testStatus, randomDog.getStatus());
	}
}
