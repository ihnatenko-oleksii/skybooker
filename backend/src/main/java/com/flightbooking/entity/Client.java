package com.flightbooking.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("CLIENT")
public class Client extends User {

    // "daneDoFaktury"
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "invoice_data_id")
    private InvoiceData invoiceData;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();

    public Client() {
    }

    public Client(Long id, String firstName, String lastName, String email, String phone, String login,
            String passwordHash, InvoiceData invoiceData) {
        super(id, firstName, lastName, email, phone, login, passwordHash);
        this.invoiceData = invoiceData;
    }

    // Methods from diagram
    public void searchFlights(String criteria) {
        // Implementation
    }

    public void createReservation(Flight flight, List<Passenger> passengers) {
        // Implementation
    }

    public void viewReservations() {
        // Implementation
    }

    public void cancelReservation(Booking booking) {
        // Implementation
    }

    public void modifyReservation(Booking booking) {
        // Implementation
    }

    @Override
    public void register() {
        // Implementation
    }

    @Override
    public void login(String login, String password) {
        // Implementation
    }

    @Override
    public void logout() {
        // Implementation
    }

    // Getters and Setters
    public InvoiceData getInvoiceData() {
        return invoiceData;
    }

    public void setInvoiceData(InvoiceData invoiceData) {
        this.invoiceData = invoiceData;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
