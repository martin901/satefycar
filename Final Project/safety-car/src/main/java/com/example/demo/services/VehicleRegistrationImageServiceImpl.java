package com.example.demo.services;

import com.example.demo.models.VehicleRegistrationImage;
import com.example.demo.repositories.VehicleRegistrationImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class VehicleRegistrationImageServiceImpl implements VehicleRegistrationImageService {
    private VehicleRegistrationImageRepository imageRepository;

    @Autowired
    public VehicleRegistrationImageServiceImpl(VehicleRegistrationImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public VehicleRegistrationImage saveImage(MultipartFile img) {
        return imageRepository.saveImage(img);
    }
}
