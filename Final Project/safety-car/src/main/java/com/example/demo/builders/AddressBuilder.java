package com.example.demo.builders;

import com.example.demo.models.Address;

public class AddressBuilder {
    private String address;

    public AddressBuilder withAddress (String address){
        this.address = address;
        return this;
    }

    public Address build(){
        return new Address(address);
    }
}
