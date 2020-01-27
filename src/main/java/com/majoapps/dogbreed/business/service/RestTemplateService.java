package com.majoapps.dogbreed.business.service;

import com.majoapps.dogbreed.business.domain.DogBreedDto;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Data
@Service
public class RestTemplateService {

    private final String RANDOM_DOG_URI = "https://dog.ceo/api/breeds/image/random";
    
    private final RestTemplate restTemplate;

    @Autowired
    public RestTemplateService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public DogBreedDto getRandomDog() throws Exception {
        return restTemplate.getForObject(RANDOM_DOG_URI, DogBreedDto.class);
    }

}
