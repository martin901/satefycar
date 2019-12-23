package com.example.demo.models;

import com.example.demo.models.Enums.RequestStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "policy_requests")
public class PolicyRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "carModel", column = @Column(name = "car_model")),
            @AttributeOverride(name = "cubicCapacity", column = @Column(name = "cubic_capacity")),
            @AttributeOverride(name = "firstRegDate", column = @Column(name = "first_registration_date")),
            @AttributeOverride(name = "driverAge", column = @Column(name = "driver_age")),
            @AttributeOverride(name = "hadAccident", column = @Column(name = "accident_prev_year"))

    })
    @Valid
    private Offer offer;

    @Column(name = "effective_date")
    @FutureOrPresent(message = "Effective date must be today or in the future")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate effectiveDate;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JsonIgnore
    private VehicleRegistrationImage image;

    @ManyToOne
    private User user;

    @Column(name = "request_date")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date requestDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @NotNull
    private RequestStatus requestStatus = RequestStatus.PENDING;

    @ManyToOne
    private User processedBy;

    public PolicyRequest() {
        requestDate = new Date();
    }

    public PolicyRequest(@Valid Offer offer, @FutureOrPresent(message = "Request date must be today or in the future") @NotNull LocalDate effectiveDate, User user, @NotNull Date requestDate) {
        this.offer = offer;
        this.effectiveDate = effectiveDate;
        this.user = user;
        this.requestDate = requestDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public VehicleRegistrationImage getImage() {
        return image;
    }

    public void setImage(VehicleRegistrationImage image) {
        this.image = image;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public User getProcessedBy() {
        return processedBy;
    }

    public void setProcessedBy(User processedBy) {
        this.processedBy = processedBy;
    }
}
