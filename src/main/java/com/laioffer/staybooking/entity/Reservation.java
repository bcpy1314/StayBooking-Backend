package com.laioffer.staybooking.entity;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "reservation")
//turn json form data to reservation obj through builder pattern by using Jackson library
@JsonDeserialize(builder = Reservation.Builder.class)
public class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id// set primary key
    @GeneratedValue(strategy = GenerationType.AUTO)// auto generated ID, such as +1
    private Long id;

    @JsonProperty("checkin_date")
    private LocalDate checkinDate;

    @JsonProperty("checkout_date")
    private LocalDate checkoutDate;

    @ManyToOne // create foreign key to build relationship between two tables
    @JoinColumn(name = "user_id")// rename the column name
    private User guest;

    @ManyToOne
    @JoinColumn(name = "stay_id")
    private Stay stay;

    //default constructor for Hibernate to generate a reservation object and set all the private field
    public Reservation() {}

    private Reservation(Builder builder) {
        this.id = builder.id;
        this.checkinDate = builder.checkinDate;
        this.checkoutDate = builder.checkoutDate;
        this.guest = builder.guest;
        this.stay = builder.stay;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getCheckinDate() {
        return checkinDate;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public User getGuest() {
        return guest;
    }

    public Reservation setGuest(User guest) {
        this.guest = guest;
        return this;
    }

    public Stay getStay() {
        return stay;
    }
    // if not having static , need to new a reservation obj
    // can create reservation without creating a reservation obj first
    public static class Builder {
        @JsonProperty("id")
        private Long id;

        @JsonProperty("checkin_date")
        private LocalDate checkinDate;

        @JsonProperty("checkout_date")
        private LocalDate checkoutDate;

        @JsonProperty("guest")
        private User guest;

        @JsonProperty("stay")
        private Stay stay;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setCheckinDate(LocalDate checkinDate) {
            this.checkinDate = checkinDate;
            return this;
        }

        public Builder setCheckoutDate(LocalDate checkoutDate) {
            this.checkoutDate = checkoutDate;
            return this;
        }

        public Builder setGuest(User guest) {
            this.guest = guest;
            return this;
        }

        public Builder setStay(Stay stay) {
            this.stay = stay;
            return this;
        }

        public Reservation build() {
            return new Reservation(this);
        }
    }
}
