package com.example.demo.builders;

import com.example.demo.models.MulticriteriaTable;

public class MulticriteriaTableBuilder {
    private int ccMin;
    private int ccMax;
    private int carAgeMin;
    private int carAgeMax;
    private double baseAmount;

    public MulticriteriaTableBuilder withCcMin(int ccMin){
        this.ccMin = ccMin;
        return this;
    }

    public MulticriteriaTableBuilder withCcMax(int ccMax){
        this.ccMax = ccMax;
        return this;
    }

    public MulticriteriaTableBuilder withCarAgeMin(int carAgeMin){
        this.carAgeMin = carAgeMin;
        return this;
    }

    public MulticriteriaTableBuilder withCarAgeMax(int carAgeMax){
        this.carAgeMax = carAgeMax;
        return this;
    }

    public MulticriteriaTableBuilder withBaseAmount(double baseAmount){
        this.baseAmount = baseAmount;
        return this;
    }

    public MulticriteriaTable build(){
        return new MulticriteriaTable(ccMin, ccMax, carAgeMin, carAgeMax, baseAmount);
    }
}
