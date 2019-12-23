package com.example.demo.servicetests;

import com.example.demo.builders.CarBrandBuilder;
import com.example.demo.models.CarBrand;
import com.example.demo.repositories.CarBrandRepository;
import com.example.demo.services.CarBrandServiceImpl;
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
public class CarBrandServiceTests {
    @Mock
    private CarBrandRepository carBrandRepository;
    @InjectMocks
    private CarBrandServiceImpl carBrandService;

    private CarBrand carBrand = new CarBrandBuilder()
            .withBrandName("Acura")
            .build();

    @Test
    public void getOne_ShouldReturnAcura(){
        when(carBrandRepository.getOne(carBrand.getId())).thenReturn(carBrand);
        CarBrand actualResult = carBrandService.getOne(carBrand.getId());
        assertThat(actualResult).isEqualTo(carBrand);
    }

    @Test
    public void getAll_ShouldReturnAllBrands(){
        when(carBrandRepository.findAll()).thenReturn(Collections.singletonList(carBrand));
        List<CarBrand> actualResult = carBrandService.getAll();
        assertThat(actualResult).containsOnly(carBrand);
    }
}
