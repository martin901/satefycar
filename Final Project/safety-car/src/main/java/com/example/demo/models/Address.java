package com.example.demo.models;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Embeddable
public class Address {
    @NotNull
    @Size(min = 5, max = 50, message = "Address must be between 5 and 50 symbols")
    private String address;

    public Address() {
    }

    public Address(@Size(min = 5, max = 50, message = "Address must be between 5 and 50 symbols") @NotNull String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
