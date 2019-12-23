package com.example.demo.servicetests;

import com.example.demo.builders.CarBrandBuilder;
import com.example.demo.builders.CarModelBuilder;
import com.example.demo.models.CarBrand;
import com.example.demo.models.CarModel;
import com.example.demo.repositories.CarModelRepository;
import com.example.demo.services.CarModelServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CarModelServiceTests {
    @Mock
    private CarModelRepository carModelRepository;
    @InjectMocks
    private CarModelServiceImpl carModelService;

    private CarBrand carBrand = new CarBrandBuilder()
            .withBrandName("BMW")
            .build();

    private CarModel carModel = new CarModelBuilder()
            .withCarBrand(carBrand)
            .withModelName("335i")
            .build();

    @Test
    public void getAllByCarBrand_ShouldReturnAllByCarBrand(){
        when(carModelRepository.findAllByCarBrand(carBrand)).thenReturn(Collections.singletonList(carModel));
        List<CarModel> actualResult = carModelService.getAllByCarBrand(carBrand);
        assertThat(actualResult).containsOnly(carModel);
    }
}
