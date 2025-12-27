package com.flightbooking.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingStatus status;

    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(name = "extra_baggage")
    private Boolean extraBaggage;

    @Column
    private Boolean insurance;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Passenger> passengers = new ArrayList<>();

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private Payment payment;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = BookingStatus.PENDING_PAYMENT;
        }
    }

    public Booking() {
    }

    public Booking(Long id, User user, Flight flight, LocalDateTime createdAt, BookingStatus status,
            BigDecimal totalPrice, String currency, Boolean extraBaggage, Boolean insurance, Payment payment) {
        this.id = id;
        this.user = user;
        this.flight = flight;
        this.createdAt = createdAt;
        this.status = status;
        this.totalPrice = totalPrice;
        this.currency = currency;
        this.extraBaggage = extraBaggage;
        this.insurance = insurance;
        this.payment = payment;
    }

    // Business Methods from Diagram

    public void addPassenger(Passenger passenger) {
        passenger.setBooking(this);
        this.passengers.add(passenger);
    }

    public void removePassenger(Passenger passenger) {
        this.passengers.remove(passenger);
        passenger.setBooking(null);
    }

    public BigDecimal calculatePrice() {
        // Delegate to Flight logic or local logic
        if (flight != null) {
            BigDecimal pricePerPerson = flight.calculatePrice(flight.getTravelClass(),
                    Boolean.TRUE.equals(extraBaggage), Boolean.TRUE.equals(insurance));
            BigDecimal total = pricePerPerson.multiply(new BigDecimal(passengers.size()));
            this.totalPrice = total;
            return total;
        }
        return BigDecimal.ZERO;
    }

    public void addExtraBaggage() {
        this.extraBaggage = true;
        calculatePrice();
    }

    public void addInsurance() {
        this.insurance = true;
        calculatePrice();
    }

    public void cancel() {
        this.status = BookingStatus.CANCELLED;
        // Restore flight seats logic could be here or service
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Boolean getExtraBaggage() {
        return extraBaggage;
    }

    public void setExtraBaggage(Boolean extraBaggage) {
        this.extraBaggage = extraBaggage;
    }

    public Boolean getInsurance() {
        return insurance;
    }

    public void setInsurance(Boolean insurance) {
        this.insurance = insurance;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }
}
