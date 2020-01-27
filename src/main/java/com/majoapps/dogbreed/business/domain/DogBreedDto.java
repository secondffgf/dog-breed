package com.majoapps.dogbreed.business.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DogBreedDto {

    @JsonProperty("status") private String status;
    @JsonProperty("message") private String message;
    
}
