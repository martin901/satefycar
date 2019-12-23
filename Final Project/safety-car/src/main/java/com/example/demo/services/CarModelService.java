package com.example.demo.services;

import com.example.demo.models.CarBrand;
import com.example.demo.models.CarModel;

import java.util.List;

public interface CarModelService {
    List<CarModel> getAllByCarBrand(CarBrand carBrand);
}
