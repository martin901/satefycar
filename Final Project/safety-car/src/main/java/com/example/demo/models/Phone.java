package com.example.demo.models;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Embeddable
public class Phone {
    @Pattern(regexp = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s./0-9]*$", message = "Please enter valid phone number")
    @Size(min = 4, max = 15, message = "Phone number must be between 4 and 15 symbols")
    @NotNull
    private String phone;

    public Phone() {
    }

    public Phone(@Pattern(regexp = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s./0-9]*$", message = "Please enter valid phone number") @Size(min = 4, max = 15, message = "Phone number must be between 4 and 15 symbols") @NotNull String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
