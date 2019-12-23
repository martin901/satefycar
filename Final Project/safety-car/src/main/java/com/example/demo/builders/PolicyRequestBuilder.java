package com.example.demo.builders;

import com.example.demo.models.Offer;
import com.example.demo.models.PolicyRequest;
import com.example.demo.models.User;

import java.time.LocalDate;
import java.util.Date;

public class PolicyRequestBuilder {
    private Offer offer;
    private LocalDate effectiveDate;
    private User user;
    private Date requestDate;

    public PolicyRequestBuilder withOffer(Offer offer) {
        this.offer = offer;
        return this;
    }

    public PolicyRequestBuilder withEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
        return this;
    }

    public PolicyRequestBuilder withUser(User user){
        this.user = user;
        return this;
    }

    public PolicyRequestBuilder withRequestDate(Date requestDate){
        this.requestDate = requestDate;
        return this;
    }

    public PolicyRequest build(){
        return new PolicyRequest(offer, effectiveDate, user, requestDate);
    }
}
