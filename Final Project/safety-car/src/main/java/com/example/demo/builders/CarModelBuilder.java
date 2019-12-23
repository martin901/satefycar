package com.example.demo.builders;

import com.example.demo.models.CarBrand;
import com.example.demo.models.CarModel;

public class CarModelBuilder {
    private CarBrand carBrand;
    private String modelName;

    public CarModelBuilder withCarBrand(CarBrand carBrand){
        this.carBrand = carBrand;
        return this;
    }

    public CarModelBuilder withModelName(String modelName){
        this.modelName = modelName;
        return this;
    }

    public CarModel build(){
        return new CarModel(carBrand, modelName);
    }
}
