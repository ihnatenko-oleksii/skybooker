package com.flightbooking.entity;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("ADMIN")
public class Administrator extends User {

    public Administrator() {
    }

    public Administrator(Long id, String firstName, String lastName, String email, String phone, String login,
            String passwordHash) {
        super(id, firstName, lastName, email, phone, login, passwordHash);
    }

}
