package com.flightbooking.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private Booking booking;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(nullable = false, length = 50)
    private String method;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;

    @Column(name = "external_tx_id", length = 100)
    private String externalTxId;

    @PrePersist
    protected void onCreate() {
        if (status == null) {
            status = PaymentStatus.NEW;
        }
    }

    public Payment() {
    }

    public Payment(Long id, Booking booking, BigDecimal amount, String currency, LocalDateTime paymentDate,
            String method, PaymentStatus status, String externalTxId) {
        this.id = id;
        this.booking = booking;
        this.amount = amount;
        this.currency = currency;
        this.paymentDate = paymentDate;
        this.method = method;
        this.status = status;
        this.externalTxId = externalTxId;
    }

    // Business Methods

    public void startPayment() { // rozpocznijPłatność()
        this.status = PaymentStatus.NEW; // Or PENDING
        this.paymentDate = LocalDateTime.now();
    }

    public void markAsConfirmed() { // oznaczJakoPotwierdzona()
        this.status = PaymentStatus.CONFIRMED;
        if (booking != null) {
            booking.setStatus(BookingStatus.PAID); // Update booking status as well?
        }
    }

    public void markAsRejected() { // oznaczJakoOdrzucona()
        this.status = PaymentStatus.REJECTED;
        if (booking != null) {
            // booking.setStatus(BookingStatus.PAYMENT_FAILED); // If exists
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getExternalTxId() {
        return externalTxId;
    }

    public void setExternalTxId(String externalTxId) {
        this.externalTxId = externalTxId;
    }
}
