package com.example.demo.models;

import com.example.demo.models.Enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "firstName", column = @Column(nullable = false)),
            @AttributeOverride(name = "lastName", column = @Column(nullable = false))
    })
    @Valid
    private Name name;

    @Email
    @Column(unique = true)
    @NotNull
    private String email;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "phone", column = @Column(name = "phone", nullable = false))
    })
    @Valid
    private Phone phone;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "address", column = @Column(nullable = false))
    })
    @Valid
    private Address address;

    @Column(name = "username", unique = true)
    @Size(min = 6, max = 30, message = "username must be between 6 and 30 symbols")
    @NotNull
    private String username;

    @Column(name = "password")
    @Size(min = 7, message = "password must be > 7 symbols")
    @NotNull
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    @NotNull
    private UserRole role;

    private boolean isEnabled;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<PolicyRequest> requests;

    public User() {
        this.role = UserRole.BASICUSER;

    }

    public User(@Valid Name name, @Email @NotNull String email, @Valid Phone phone, @Valid Address address, @Size(min = 6, max = 30, message = "username must be between 6 and 30 symbols") @NotNull String username, @Size(min = 7, message = "password must be > 7 symbols") @NotNull String password, @NotNull UserRole role, boolean isEnabled) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.username = username;
        this.password = password;
        this.role = role;
        this.isEnabled = isEnabled;
    }

    public boolean isAdmin() {
        return this.role == UserRole.ADMIN;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public List<PolicyRequest> getRequests() {
        return requests;
    }

    public void setRequests(List<PolicyRequest> requests) {
        this.requests = requests;
    }
}
