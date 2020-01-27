package com.majoapps.dogbreed.business.service;

import com.majoapps.dogbreed.business.domain.DogBreedDto;
import com.majoapps.dogbreed.business.domain.DogBreedResponse;
import com.majoapps.dogbreed.data.entity.DogBreed;
import com.majoapps.dogbreed.data.repository.DogBreedRepository;
import com.majoapps.dogbreed.exception.ResourceNotFoundException;
import com.majoapps.dogbreed.utility.UrlParser;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Data
@Service
public class DogBreedService {

    private final DogBreedRepository dogBreedRepository;
    private final RestTemplateService restTemplateService;
    private final ModelMapper modelMapper;
    private final AWSService awsService;

    @Autowired
    public DogBreedService(
            DogBreedRepository dogBreedRepository, 
            RestTemplateService restTemplateService,
            ModelMapper modelMapper,
            AWSService awsService) {
        this.dogBreedRepository = dogBreedRepository;
        this.restTemplateService = restTemplateService;
        this.modelMapper = modelMapper;
        this.awsService = awsService;
    }

    public List<String> getAllBreeds() {
        List<DogBreed> dogBreedListResponse = dogBreedRepository.findAll();
        ArrayList<String> stringList = new ArrayList<>();
        for (DogBreed dogBreedItem : dogBreedListResponse) {
            stringList.add(dogBreedItem.getName());
        }
        return (stringList);
    }

    public List<DogBreedResponse> getAll() {
        List<DogBreed> dogBreedListResponse = dogBreedRepository.findAll();

        return dogBreedListResponse.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    public DogBreed getDogBreedById(UUID id) {
        Objects.requireNonNull(id);
        return this.dogBreedRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DogBreed " + id + " not found"));
    }

    public List<DogBreed> getDogBreedByName(String name) {
        Objects.requireNonNull(name);
        return dogBreedRepository.findByName(name);
    }

    public ResponseEntity<?> deleteDogBreedById(UUID id) {
        return this.dogBreedRepository.findById(id).map(dogBreed -> {
            dogBreedRepository.delete(dogBreed);
            awsService.deleteFile(dogBreed.getName());
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("DogBreed " + id + " not found"));
    }

    public DogBreed saveRandomDogBreed() {
        DogBreedDto dogBreedResponse;
        try {
            dogBreedResponse = restTemplateService.getRandomDog();
            List<DogBreed> dogBreedListResponse = dogBreedRepository
                .findByName(dogBreedResponse.getMessage());

            DogBreed dogBreedEntity = new DogBreed();
        
            if (dogBreedListResponse.isEmpty()) { //add new Dog Breed as it doesn't exist
                if (UrlParser.dogNameFromUrl(dogBreedResponse.getMessage()) != null) {   
                    dogBreedEntity.setName(UrlParser.dogNameFromUrl(dogBreedResponse.getMessage()));
                    URL photoURL = awsService.addFile(
                        UrlParser.dogNameFromUrl(dogBreedResponse.getMessage()),
                        dogBreedResponse.getMessage()
                    );  
                    dogBreedEntity.setStorageLocation(photoURL.toString());
                    dogBreedEntity = dogBreedRepository.save(dogBreedEntity);
                } else {
                    throw new ResourceNotFoundException("Error " + dogBreedResponse.getMessage());
                }
            } else { //update existing using index 0 as there should only be one type
                throw new ResourceNotFoundException("Duplicate entry " + 
                    dogBreedListResponse.get(0).getName());
            }
            return dogBreedEntity;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResourceNotFoundException("Exception " + e.getLocalizedMessage());
        } 
        
    }

    private DogBreedResponse convertToDto(DogBreed entity) {
        return(modelMapper.map(entity, DogBreedResponse.class));
    }

}
