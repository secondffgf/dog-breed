package com.majoapps.dogbreed.web;

import com.majoapps.dogbreed.business.domain.DogBreedResponse;
import com.majoapps.dogbreed.business.service.DogBreedService;
import com.majoapps.dogbreed.data.entity.DogBreed;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/api/v1/dog")
public class DogServiceController {

    private final DogBreedService dogBreedService;

    @Autowired
    private DogServiceController(DogBreedService dogBreedService){
        this.dogBreedService = dogBreedService;
    }

    @RequestMapping(method= RequestMethod.GET)
    public ResponseEntity<List<String>> getAllDogBreed() {
        try {
            List<String> dogBreedList = this.dogBreedService.getAllBreeds();
            if (dogBreedList == null || dogBreedList.isEmpty()) {
              return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return ResponseEntity.ok(dogBreedList);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<List<DogBreedResponse>> getAllDogBreeds() {
        try {
            List<DogBreedResponse> dogBreedList = this.dogBreedService.getAll();
            if (dogBreedList == null || dogBreedList.isEmpty()) {
              return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return ResponseEntity.ok(dogBreedList);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public DogBreed getDogBreedById(@PathVariable(value="id") UUID id) {
        return this.dogBreedService.getDogBreedById(id);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<List<DogBreed>> getByBreedName(@RequestParam(value="name") String name) {
        try {
            List<DogBreed> dogBreedList = this.dogBreedService.getDogBreedByName(name);
            if (dogBreedList == null || dogBreedList.isEmpty()) {
              return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return ResponseEntity.ok(dogBreedList);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(code = HttpStatus.CREATED)
    public DogBreed addRandomDogBreed() {
        return this.dogBreedService.saveRandomDogBreed();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteDogBreedById(@PathVariable(value="id") UUID id) {
        return this.dogBreedService.deleteDogBreedById(id);
    }

}
