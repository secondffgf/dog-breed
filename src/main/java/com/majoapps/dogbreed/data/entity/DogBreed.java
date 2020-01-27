package com.majoapps.dogbreed.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper=true)
@Table(name="DOGBREED")
public class DogBreed extends AuditModel{
    private static final long serialVersionUID = -7029517068565054771L;

    @Column(name = "name")
    private String name;

    @Column(name="storage_location")
    private String storageLocation;
}
