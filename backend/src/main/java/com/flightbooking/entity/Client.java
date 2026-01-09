package com.flightbooking.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("CLIENT")
public class Client extends User {

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
