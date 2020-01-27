package com.majoapps.dogbreed.data.repository;

import com.majoapps.dogbreed.data.entity.DogBreed;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DogBreedRepository extends JpaRepository <DogBreed, UUID> {
    List<DogBreed> findByName(String dogBreedString);
}
