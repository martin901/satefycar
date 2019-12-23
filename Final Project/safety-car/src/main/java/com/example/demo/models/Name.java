package com.example.demo.models;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Embeddable
public class Name {
    @Pattern(regexp = "([A-Z'a-z -]+)", message = "Please enter valid first name")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 symbols")
    @NotNull
    private String firstName;

    @Pattern(regexp = "([A-Z'a-z -]+)", message = "Please enter valid last name")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 symbols")
    @NotNull
    private String lastName;

    public Name() {
    }

    public Name(@Pattern(regexp = "([A-Z'a-z -]+)", message = "Please enter valid first name") @Size(min = 2, max = 50, message = "First name must be between 2 and 50 symbols") @NotNull String firstName, @Pattern(regexp = "([A-Z'a-z -]+)", message = "Please enter valid last name") @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 symbols") @NotNull String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return this.firstName + " " + this.lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
