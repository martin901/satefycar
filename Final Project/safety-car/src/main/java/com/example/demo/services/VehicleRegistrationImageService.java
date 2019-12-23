package com.example.demo.services;

import com.example.demo.models.VehicleRegistrationImage;
import org.springframework.web.multipart.MultipartFile;

public interface VehicleRegistrationImageService {
    VehicleRegistrationImage saveImage(MultipartFile img);
}
