package com.example.demo.builders;

import com.example.demo.models.Phone;

public class PhoneBuilder {
    private String phone;

    public PhoneBuilder withPhone (String phone){
        this.phone = phone;
        return this;
    }

    public Phone build() {
        return new Phone(phone);
    }
}
