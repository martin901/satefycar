package com.example.demo.builders;

import com.example.demo.models.Address;
import com.example.demo.models.Enums.UserRole;
import com.example.demo.models.Name;
import com.example.demo.models.Phone;
import com.example.demo.models.User;

public class UserBuilder {
    private Name name;
    private String email;
    private Phone phone;
    private Address address;
    private String username;
    private String password;
    private UserRole role;

    public UserBuilder withName(Name name){
        this.name = name;
        return this;
    }

    public UserBuilder withEmail(String email){
        this.email = email;
        return this;
    }

    public UserBuilder withPhone(Phone phone){
        this.phone = phone;
        return this;
    }

    public UserBuilder withAddress(Address address){
        this.address = address;
        return this;
    }

    public UserBuilder withUsername(String username){
        this.username = username;
        return this;
    }

    public UserBuilder withPassword (String password){
        this.password = password;
        return this;
    }

    public UserBuilder withRole (UserRole role){
        this.role = role;
        return this;
    }


    public User build(){
        return new User(name, email, phone, address, username, password, role, true);
    }
}
