package com.example.demo.repositories;

import com.example.demo.models.VehicleRegistrationImage;
import org.springframework.web.multipart.MultipartFile;

public interface VehicleRegistrationImageRepository {
    VehicleRegistrationImage saveImage(MultipartFile img);
}
