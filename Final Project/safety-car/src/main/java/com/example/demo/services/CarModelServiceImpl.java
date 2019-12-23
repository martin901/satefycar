package com.example.demo.services;

import com.example.demo.models.CarBrand;
import com.example.demo.models.CarModel;
import com.example.demo.repositories.CarModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarModelServiceImpl implements CarModelService {
    private CarModelRepository carModelRepository;

    @Autowired
    public CarModelServiceImpl(CarModelRepository carModelRepository) {
        this.carModelRepository = carModelRepository;
    }

    @Override
    public List<CarModel> getAllByCarBrand(CarBrand carBrand) {
        return carModelRepository.findAllByCarBrand(carBrand);
    }
}
