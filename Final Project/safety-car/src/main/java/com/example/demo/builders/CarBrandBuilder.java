package com.example.demo.builders;

import com.example.demo.models.CarBrand;

public class CarBrandBuilder {
    private String brandName;

    public CarBrandBuilder withBrandName(String brandName){
        this.brandName = brandName;
        return this;
    }

    public CarBrand build(){
        return new CarBrand(brandName);
    }
}
