package com.example.demo.builders;

import com.example.demo.models.CarModel;
import com.example.demo.models.Offer;

import java.time.LocalDate;

public class OfferBuilder {
    private CarModel carModel;
    private int cubicCapacity;
    private LocalDate firstRegDate;
    private int driverAge;
    private boolean hadAccident;
    private Double totalPremium;

    public OfferBuilder withCarModel(CarModel carModel){
        this.carModel = carModel;
        return this;
    }

    public OfferBuilder withCubicCapacity(int cubicCapacity){
        this.cubicCapacity = cubicCapacity;
        return this;
    }

    public OfferBuilder withFirstRegDate(LocalDate firstRegDate){
        this.firstRegDate = firstRegDate;
        return this;
    }

    public OfferBuilder withDriverAge(int driverAge){
        this.driverAge = driverAge;
        return this;
    }

    public OfferBuilder withHadAccident (boolean hadAccident){
        this.hadAccident = hadAccident;
        return this;
    }

    public OfferBuilder withTotalPremium (Double totalPremium){
        this.totalPremium = totalPremium;
        return this;
    }

    public Offer build(){
        return new Offer(carModel, cubicCapacity, firstRegDate, driverAge, hadAccident, totalPremium);
    }
}
