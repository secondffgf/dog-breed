package com.majoapps.dogbreed.business.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.majoapps.dogbreed.business.domain.DogBreedDto;
import com.majoapps.dogbreed.business.domain.DogBreedResponse;
import com.majoapps.dogbreed.data.entity.DogBreed;
import com.majoapps.dogbreed.data.repository.DogBreedRepository;
import com.majoapps.dogbreed.exception.ResourceNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class DogBreedServiceTest {
	private static final String BULLDOG = "Bulldog";
	
	@Mock
	private DogBreedRepository dogBreedRepository;
	
	@Mock
	private RestTemplateService restTemplateService;
	
	@Mock
	private ModelMapper modelMapper;
	
	@Mock
	private AWSService awsService;
	
	@InjectMocks
	private DogBreedService dogBreedService;
	
	@BeforeEach
	void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testGetAllBreeds() {
		DogBreed bulldog = new DogBreed();
		bulldog.setName(BULLDOG);
		List<DogBreed> breeds = Arrays.asList(bulldog);
		
		when(dogBreedRepository.findAll())
			.thenReturn(breeds);
		
		List<String> allBreeds = dogBreedService.getAllBreeds();
		assertTrue(allBreeds.contains(BULLDOG));
	}

	@Test
	public void testGetAll() {
		DogBreed bulldog = new DogBreed();
		bulldog.setName(BULLDOG);
		List<DogBreed> breeds = Arrays.asList(bulldog);
		
		DogBreedResponse response = new DogBreedResponse();
		response.setName(BULLDOG);
		
		when(dogBreedRepository.findAll())
			.thenReturn(breeds);
		Class<DogBreedResponse> responseClass = DogBreedResponse.class;
		when(modelMapper.map(any(DogBreed.class), eq(responseClass)))
			.thenReturn(response);
		
		List<DogBreedResponse> allBreeds = dogBreedService.getAll();
		assertEquals(BULLDOG, allBreeds.get(0).getName());
	}
	
	@Test
	public void testGetDogBreedByIdReturnsSuccess() {
		String uuid = "38400000-8cf0-11bd-b23e-10b96e4ef00d";
		UUID id = UUID.fromString(uuid);
		DogBreed bulldog = new DogBreed();
		bulldog.setName(BULLDOG);
		
		when(dogBreedRepository.findById(any()))
			.thenReturn(Optional.of(bulldog));
		DogBreed dogBreedById = dogBreedService.getDogBreedById(id);
		assertEquals(BULLDOG, dogBreedById.getName());
	}
	
	@Test
	public void testGetDogBreedByIdThrowsError() {
		String uuid = "38400000-8cf0-11bd-b23e-10b96e4ef00d";
		UUID id = UUID.fromString(uuid);
		when(dogBreedRepository.findById(any()))
			.thenReturn(Optional.empty());
		
		assertThrows(ResourceNotFoundException.class, () -> {
			dogBreedService.getDogBreedById(id);
		});
	}
	
	@Test
	public void testGetDogBreedById() {
		DogBreed bulldog = new DogBreed();
		bulldog.setName(BULLDOG);
		List<DogBreed> breeds = Arrays.asList(bulldog);
		
		when(dogBreedRepository.findByName(any()))
			.thenReturn(breeds);
		
		List<DogBreed> dogBreedByName = dogBreedService.getDogBreedByName(BULLDOG);
		assertEquals(BULLDOG, dogBreedByName.get(0).getName());
	}
	
	@Test
	public void testDeleteDogBreedSuccess() {
		String uuid = "38400000-8cf0-11bd-b23e-10b96e4ef00d";
		UUID id = UUID.fromString(uuid);
		DogBreed bulldog = new DogBreed();
		bulldog.setName(BULLDOG);
		
		when(dogBreedRepository.findById(any()))
			.thenReturn(Optional.of(bulldog));
		
		doNothing().when(dogBreedRepository).delete(any());
		doNothing().when(awsService).deleteFile(any());
		
		ResponseEntity<?> response = dogBreedService.deleteDogBreedById(id);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void testDeleteDogBreedThrowsError() {
		String uuid = "38400000-8cf0-11bd-b23e-10b96e4ef00d";
		UUID id = UUID.fromString(uuid);
		when(dogBreedRepository.findById(any()))
			.thenReturn(Optional.empty());
		
		assertThrows(ResourceNotFoundException.class, () -> {
			dogBreedService.deleteDogBreedById(id);
		});
	}
	
	@Test
	public void testSaveRandomDogBreedDuplicateEntryException() throws Exception {
		String testMessage = "test message";
		String testStatus = "test status";
		DogBreedDto dogBreedWrapper = new DogBreedDto();
		dogBreedWrapper.setMessage(testMessage);
		dogBreedWrapper.setStatus(testStatus);
		when(restTemplateService.getRandomDog())
			.thenReturn(dogBreedWrapper);
		
		DogBreed dogBreed = new DogBreed();
		dogBreed.setName(BULLDOG);
		
		when(dogBreedRepository.findByName(any()))
			.thenReturn(Arrays.asList(dogBreed));
		
		assertThrows(ResourceNotFoundException.class, () -> {
			dogBreedService.saveRandomDogBreed();
		});
	}
	
	@Test
	public void testSaveRandomDogBreedBadUrl() throws Exception {
		String testMessage = "test message";
		String testStatus = "test status";
		DogBreedDto dogBreedWrapper = new DogBreedDto();
		dogBreedWrapper.setMessage(testMessage);
		dogBreedWrapper.setStatus(testStatus);
		when(restTemplateService.getRandomDog())
			.thenReturn(dogBreedWrapper);
		
		DogBreed dogBreed = new DogBreed();
		dogBreed.setName(BULLDOG);
		
		when(dogBreedRepository.findByName(any()))
			.thenReturn(new ArrayList<DogBreed>());
		
		assertThrows(ResourceNotFoundException.class, () -> {
			dogBreedService.saveRandomDogBreed();
		});
	}
	
	@Test
	public void testSaveRandomDogBreedSuccess() throws Exception {
		String testMessage = "breeds/bulldog/";
		String testStatus = "test status";
		DogBreedDto dogBreedWrapper = new DogBreedDto();
		dogBreedWrapper.setMessage(testMessage);
		dogBreedWrapper.setStatus(testStatus);
		when(restTemplateService.getRandomDog())
			.thenReturn(dogBreedWrapper);
		
		DogBreed dogBreed = new DogBreed();
		dogBreed.setName(BULLDOG);
		
		when(dogBreedRepository.findByName(any()))
			.thenReturn(new ArrayList<DogBreed>());
		
		when(awsService.addFile(any(), any()))
			.thenReturn(new URL("http://test_url"));
		
		DogBreed breed = new DogBreed();
		breed.setName(BULLDOG);
		breed.setStorageLocation(testMessage);
		when(dogBreedRepository.save(any()))
			.thenReturn(breed);
		
		DogBreed randomDogBreed = dogBreedService.saveRandomDogBreed();
		assertEquals(testMessage, breed.getStorageLocation());
		assertEquals(BULLDOG, randomDogBreed.getName());
	}
}
