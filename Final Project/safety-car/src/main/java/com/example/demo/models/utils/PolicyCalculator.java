package com.example.demo.models.utils;

import com.example.demo.models.MulticriteriaTable;
import com.example.demo.models.Offer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Component
@Scope("session")
public class PolicyCalculator {
    @Valid
    private Offer offer;

    private List<MulticriteriaTable> multicriteriaModels;

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public PolicyCalculator() {
        multicriteriaModels = MulticriteriaTableHelper.getMulticriteriaTable();
    }

    private Double calculateBaseAmount() {
        Double baseAmount = null;
        LocalDate today = LocalDate.now();
        int carAge = Period.between(offer.getFirstRegDate(), today).getYears();

        for (int i = 0; i < multicriteriaModels.size() - 1; i++) {
            if (this.offer.getCubicCapacity() >= multicriteriaModels.get(i).getCcMin() && this.offer.getCubicCapacity() <= multicriteriaModels.get(i).getCcMax()) {
                if (carAge >= multicriteriaModels.get(i).getCarAgeMin() && carAge <= multicriteriaModels.get(i).getCarAgeMax()) {
                    baseAmount = multicriteriaModels.get(i).getBaseAmount();
                    break;
                } else {
                    baseAmount = multicriteriaModels.get(i + 1).getBaseAmount();
                    break;
                }
            }
        }
        return baseAmount;
    }

    private double calculateNetPremium() {
        double baseAmount;
        baseAmount = calculateBaseAmount();

        double netPremium = baseAmount;
        if (this.offer.isHadAccident()) {
            netPremium += baseAmount * 0.2;
        }
        if (this.offer.getDriverAge() < 25) {
            netPremium += baseAmount * 0.05;
        }
        return Math.ceil(netPremium * 100) / 100;
    }

    public double calculateTotalPremium() {
        double netPremium = calculateNetPremium();
        return Math.ceil(netPremium * 1.1 * 100) / 100;
    }
}
