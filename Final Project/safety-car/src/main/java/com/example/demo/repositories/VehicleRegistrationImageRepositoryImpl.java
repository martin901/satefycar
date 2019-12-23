package com.example.demo.repositories;

import com.example.demo.models.VehicleRegistrationImage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Repository
public class VehicleRegistrationImageRepositoryImpl implements VehicleRegistrationImageRepository {
    private SessionFactory sessionFactory;

    @Autowired
    public VehicleRegistrationImageRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public VehicleRegistrationImage saveImage(MultipartFile img) {
        Session session = sessionFactory.getCurrentSession();
        try{
            byte[] imgBytes = img.getBytes();
            InputStream inputStream = new ByteArrayInputStream(imgBytes);
            inputStream.read(imgBytes);
            inputStream.close();

            VehicleRegistrationImage image = new VehicleRegistrationImage();
            session.doWork(connection -> image.setImage(imgBytes));
            session.save(image);
            return image;
        } catch (Exception e){
            throw new IllegalArgumentException("Error saving image");
        }
    }
}
