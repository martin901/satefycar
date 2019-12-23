package com.example.demo.models;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "carmodels")
@Embeddable
public class CarModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private CarBrand carBrand = new CarBrand();

    @Column(name = "modelName")
    @NotNull
    private String modelName;

    @Column(name = "code")
    @NotNull
    private String code;

    public CarModel() {
    }

    public CarModel(CarBrand carBrand, @NotNull String modelName) {
        this.carBrand = carBrand;
        this.modelName = modelName;
    }

    @Override
    public String toString() {
        return carBrand.getBrandName() + " " + modelName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public CarBrand getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(CarBrand carBrand) {
        this.carBrand = carBrand;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
