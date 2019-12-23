package com.example.demo.services;

import com.example.demo.models.CarBrand;
import com.example.demo.repositories.CarBrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarBrandServiceImpl implements CarBrandService {
    private CarBrandRepository carBrandRepository;

    @Autowired
    public CarBrandServiceImpl(CarBrandRepository carBrandRepository) {
        this.carBrandRepository = carBrandRepository;
    }

    @Override
    public CarBrand getOne(Long id) {
        return carBrandRepository.getOne(id);
    }

    @Override
    public List<CarBrand> getAll() {
        return carBrandRepository.findAll();
    }
}
