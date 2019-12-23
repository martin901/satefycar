package com.example.demo.services;

import com.example.demo.models.CarBrand;

import java.util.List;

public interface CarBrandService {
    CarBrand getOne(Long id);
    List<CarBrand> getAll();
}
