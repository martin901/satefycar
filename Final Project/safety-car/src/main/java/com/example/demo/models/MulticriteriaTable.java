package com.example.demo.models;

import javax.persistence.*;

@Entity
@Table(name = "multicriteria_table")
public class MulticriteriaTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "cc_min")
    private int ccMin;

    @Column(name = "cc_max")
    private int ccMax;

    @Column(name = "car_age_min")
    private int carAgeMin;

    @Column(name = "car_age_max")
    private int carAgeMax;
    @Column(name = "base_amount")
    private double baseAmount;

    public MulticriteriaTable() {
    }

    public MulticriteriaTable(int ccMin, int ccMax, int carAgeMin, int carAgeMax, double baseAmount) {
        this.ccMin = ccMin;
        this.ccMax = ccMax;
        this.carAgeMin = carAgeMin;
        this.carAgeMax = carAgeMax;
        this.baseAmount = baseAmount;
    }

    @Override
    public String toString() {
        return ccMin + " " + ccMax + " " + carAgeMin + " " + carAgeMax + " " + baseAmount + "\n";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCcMin() {
        return ccMin;
    }

    public void setCcMin(int ccMin) {
        this.ccMin = ccMin;
    }

    public int getCcMax() {
        return ccMax;
    }

    public void setCcMax(int ccMax) {
        this.ccMax = ccMax;
    }

    public int getCarAgeMin() {
        return carAgeMin;
    }

    public void setCarAgeMin(int carAgeMin) {
        this.carAgeMin = carAgeMin;
    }

    public int getCarAgeMax() {
        return carAgeMax;
    }

    public void setCarAgeMax(int carAgeMax) {
        this.carAgeMax = carAgeMax;
    }

    public double getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(double baseAmount) {
        this.baseAmount = baseAmount;
    }
}
