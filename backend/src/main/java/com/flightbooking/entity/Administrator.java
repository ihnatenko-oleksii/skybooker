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

    // Methods from diagram
    public void addFlight(Flight flight) {
        // Implementation
    }

    public void editFlight(Flight flight) {
        // Implementation
    }

    public void removeFlight(Flight flight) {
        // Implementation
    }

    public void viewReservations() {
        // Implementation
    }

    public void manageUsers() {
        // Implementation
    }

    @Override
    public void register() {
        // Implementation
        // Admin registration logic
    }

    @Override
    public void login(String login, String password) {
        // Implementation
    }

    @Override
    public void logout() {
        // Implementation
    }
}
