package com.example.demo.servicetests;

import com.example.demo.models.VehicleRegistrationImage;
import com.example.demo.repositories.VehicleRegistrationImageRepository;
import com.example.demo.services.VehicleRegistrationImageServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VehicleRegistrationImageServiceTests {
    @Mock
    private VehicleRegistrationImageRepository imageRepository;
    @InjectMocks
    private VehicleRegistrationImageServiceImpl vehicleRegistrationImageService;

    @Test
    public void saveImage_ShouldSaveImage(){
        VehicleRegistrationImage image = new VehicleRegistrationImage();
        MockMultipartFile mockImg = new MockMultipartFile("image", "", "image/jpeg", "some text".getBytes());
        when(imageRepository.saveImage(mockImg)).thenReturn(image);
        VehicleRegistrationImage actualResult = vehicleRegistrationImageService.saveImage(mockImg);
        assertThat(actualResult).isEqualTo(image);
    }
}
