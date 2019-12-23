package com.example.demo.models;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Embeddable
public class Offer {
    @ManyToOne
    private CarModel carModel;

    @Range(min = 1, max = 999999, message = "Invalid cubic capacity input")
    @NotNull
    private Integer cubicCapacity;

    @PastOrPresent(message = "First Registration date must be today or in the past")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate firstRegDate;

    @NotNull
    @Range(min = 18, message = "Driver should be an adult")
    private Integer driverAge;

    @NotNull
    private boolean hadAccident;

    private Double totalPremium;

    public Offer() {
    }

    public Offer(CarModel carModel, @Range(min = 1, max = 999999, message = "Invalid cubic capacity input") @NotNull int cubicCapacity, @PastOrPresent(message = "First Registration date must be today or in the past") @NotNull LocalDate firstRegDate, @NotNull @Range(min = 18, message = "Driver should be an adult") int driverAge, @NotNull boolean hadAccident, Double totalPremium) {
        this.carModel = carModel;
        this.cubicCapacity = cubicCapacity;
        this.firstRegDate = firstRegDate;
        this.driverAge = driverAge;
        this.hadAccident = hadAccident;
        this.totalPremium = totalPremium;
    }

    public CarModel getCarModel() {
        return carModel;
    }

    public void setCarModel(CarModel carModel) {
        this.carModel = carModel;
    }

    public Integer getCubicCapacity() {
        return cubicCapacity;
    }

    public void setCubicCapacity(Integer cubicCapacity) {
        this.cubicCapacity = cubicCapacity;
    }

    public LocalDate getFirstRegDate() {
        return firstRegDate;
    }

    public void setFirstRegDate(LocalDate firstRegDate) {
        this.firstRegDate = firstRegDate;
    }

    public Integer getDriverAge() {
        return driverAge;
    }

    public void setDriverAge(int driverAge) {
        this.driverAge = driverAge;
    }

    public boolean isHadAccident() {
        return hadAccident;
    }

    public void setHadAccident(boolean hadAccident) {
        this.hadAccident = hadAccident;
    }

    public void setDriverAge(Integer driverAge) {
        this.driverAge = driverAge;
    }

    public Double getTotalPremium() {
        return totalPremium;
    }

    public void setTotalPremium(Double totalPremium) {
        this.totalPremium = totalPremium;
    }
}
