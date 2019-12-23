package com.example.demo.repositories;

import com.example.demo.models.CarBrand;
import com.example.demo.models.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarModelRepository extends JpaRepository<CarModel, Long> {
     List<CarModel> findAllByCarBrand(CarBrand carBrand);
}
