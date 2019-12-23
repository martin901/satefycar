package com.example.demo.builders;

import com.example.demo.models.Name;

public class NameBuilder {
    private String firstName;
    private String lastName;

    public NameBuilder withFirstName(String firstName){
        this.firstName = firstName;
        return this;
    }

    public NameBuilder withLastName (String lastName){
        this.lastName = lastName;
        return this;
    }

    public Name build(){
        return new Name(firstName, lastName);
    }
}
